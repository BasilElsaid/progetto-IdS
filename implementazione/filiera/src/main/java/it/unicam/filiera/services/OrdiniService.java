package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateOrdineItemRequest;
import it.unicam.filiera.dto.response.OrdineResponse;
import it.unicam.filiera.domain.*;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrdiniService {

    private final OrdiniRepository ordineRepo;
    private final UtentiRepository utentiRepository;
    private final AnnunciProdottiRepository prodottiRepo;
    private final AnnunciPacchettiRepository pacchettiRepo;
    private final SpedizioniAsyncService spedizioniAsyncService;

    public OrdiniService(OrdiniRepository ordineRepo,
                         UtentiRepository utentiRepository,
                         AnnunciProdottiRepository prodottiRepo,
                         AnnunciPacchettiRepository pacchettiRepo,
                         SpedizioniAsyncService spedizioniAsyncService) {
        this.ordineRepo = ordineRepo;
        this.utentiRepository = utentiRepository;
        this.prodottiRepo = prodottiRepo;
        this.pacchettiRepo = pacchettiRepo;
        this.spedizioniAsyncService = spedizioniAsyncService;
    }

    @Transactional
    public OrdineResponse creaOrdine(List<CreateOrdineItemRequest> items) {

        if (items == null || items.isEmpty())
            throw new BadRequestException("Items mancanti");

        Acquirente acquirente = getAcquirenteLoggato();

        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        ordine.setStato(StatoOrdine.IN_CREAZIONE);
        ordine.setDataCreazione(LocalDateTime.now());

        double totale = 0.0;

        for (CreateOrdineItemRequest r : items) {

            if (r.quantita() <= 0)
                throw new BadRequestException("Quantita non valida");

            OrdineItem item = new OrdineItem();
            item.setAnnuncioId(r.annuncioId());
            item.setPacchetto(r.pacchetto());
            item.setQuantita(r.quantita());

            if (r.pacchetto()) {

                var annuncio = pacchettiRepo.findById(r.annuncioId())
                        .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));

                if (!annuncio.isAttivo() || annuncio.getStock() < r.quantita())
                    throw new BadRequestException("Pacchetto non disponibile");

                item.setPrezzoUnitario(annuncio.getPrezzo());

            } else {

                var annuncio = prodottiRepo.findById(r.annuncioId())
                        .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

                if (!annuncio.isAttivo() || annuncio.getStock() < r.quantita())
                    throw new BadRequestException("Prodotto non disponibile");

                item.setPrezzoUnitario(annuncio.getPrezzo());
            }

            ordine.aggiungiItem(item);
            totale += item.getTotaleRiga();
        }

        ordine.setTotale(totale);
        ordine.setStato(StatoOrdine.IN_ATTESA_PAGAMENTO);

        Ordine saved = ordineRepo.save(ordine);

        return OrdineResponse.from(saved);
    }

    @Transactional
    public OrdineResponse pagaOrdine(Long ordineId) {

        if (ordineId == null)
            throw new BadRequestException("ordineId mancante");

        Acquirente acquirente = getAcquirenteLoggato();

        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirente.getId()))
            throw new ForbiddenException("Ordine non appartiene a questo acquirente");

        if (ordine.getStato() != StatoOrdine.IN_ATTESA_PAGAMENTO)
            throw new BadRequestException("Ordine non in attesa pagamento");

        // Controllo e aggiornamento stock
        for (OrdineItem item : ordine.getItems()) {

            if (item.isPacchetto()) {

                var pacchetto = pacchettiRepo.findById(item.getAnnuncioId())
                        .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));

                if (!pacchetto.isAttivo() || pacchetto.getStock() < item.getQuantita())
                    throw new BadRequestException("Pacchetto non disponibile o stock insufficiente");

                pacchetto.setStock(pacchetto.getStock() - item.getQuantita());
                pacchettiRepo.save(pacchetto);

            } else {

                var prodotto = prodottiRepo.findById(item.getAnnuncioId())
                        .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

                if (!prodotto.isAttivo() || prodotto.getStock() < item.getQuantita())
                    throw new BadRequestException("Prodotto non disponibile o stock insufficiente");

                prodotto.setStock(prodotto.getStock() - item.getQuantita());
                prodottiRepo.save(prodotto);
            }
        }

        ordine.setStato(StatoOrdine.PAGATO);
        //ordine.setMetodoPagamento(metodo);
        ordine.setDataPagamento(LocalDateTime.now());

        Ordine saved = ordineRepo.save(ordine);

        // gestione spedizione async
        spedizioniAsyncService.gestisciOrdineAutomatico(saved.getId());

        return OrdineResponse.from(saved);
    }

    @Transactional
    public void eliminaOrdine(Long ordineId) {

        Acquirente acquirente = getAcquirenteLoggato();

        if (ordineId == null)
            throw new BadRequestException("ordineId mancante");

        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirente.getId()))
            throw new ForbiddenException("Ordine non appartiene a questo acquirente");

        if (ordine.getStato() == StatoOrdine.PAGATO)
            throw new BadRequestException("Ordine pagato non eliminabile");

        ordineRepo.delete(ordine);
    }

    public List<OrdineResponse> getAllOrdini() {

        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }

        if (user instanceof GestorePiattaforma) {
            return StreamSupport.stream(ordineRepo.findAll().spliterator(), false)
                    .map(OrdineResponse::from)
                    .toList();
        }

        if (user instanceof Acquirente acquirente) {
            return ordineRepo.findByAcquirenteId(acquirente.getId())
                    .stream()
                    .map(OrdineResponse::from)
                    .toList();
        }

        throw new ForbiddenException("Non puoi visualizzare gli ordini");
    }

    public OrdineResponse getById(Long id) {
        return OrdineResponse.from(
                ordineRepo.findById(id).orElseThrow(() -> new NotFoundException("Ordine non trovato"))
        );
    }

    public List<OrdineResponse> all() {
        return StreamSupport.stream(ordineRepo.findAll().spliterator(), false)
                .map(OrdineResponse::from)
                .collect(Collectors.toList());
    }

    public List<OrdineResponse> getByAcquirente(Long acquirenteId) {
        return ordineRepo.findByAcquirenteId(acquirenteId).stream()
                .map(OrdineResponse::from)
                .collect(Collectors.toList());
    }

    // HELPER
    private Acquirente getAcquirenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }

        if (!(user instanceof Acquirente acquirente)) {
            throw new ForbiddenException("Solo un acquirente può effettuare questa operazione");
        }

        return acquirente;
    }
}