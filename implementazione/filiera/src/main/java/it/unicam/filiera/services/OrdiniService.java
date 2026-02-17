package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateOrdineItemRequest;
import it.unicam.filiera.dto.response.OrdineResponse;
import it.unicam.filiera.domain.*;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.*;
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
    public OrdineResponse creaOrdine(Long acquirenteId, List<CreateOrdineItemRequest> items) {
        if (acquirenteId == null) throw new BadRequestException("acquirenteId mancante");
        if (items == null || items.isEmpty()) throw new BadRequestException("items mancanti");

        UtenteGenerico u = utentiRepository.findById(acquirenteId)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        if (!(u instanceof Acquirente acquirente)) {
            throw new BadRequestException("L'utente selezionato non è un acquirente");
        }

        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        ordine.setStato(StatoOrdine.IN_CREAZIONE);
        ordine.setDataCreazione(LocalDateTime.now());

        double totale = 0.0;

        for (CreateOrdineItemRequest r : items) {
            if (r.quantita() <= 0) throw new BadRequestException("Quantita non valida");

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
    public OrdineResponse pagaOrdine(Long acquirenteId, Long ordineId, MetodoPagamento metodo) {
        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId))
            throw new BadRequestException("Ordine non appartiene a questo acquirente");

        if (ordine.getStato() != StatoOrdine.IN_ATTESA_PAGAMENTO)
            throw new BadRequestException("Ordine non in attesa pagamento");

        // Controllo disponibilità degli item in base al tipo
        for (OrdineItem item : ordine.getItems()) {

            if (item.isPacchetto()) {
                var pacchettoAnnuncio = pacchettiRepo.findById(item.getAnnuncioId())
                        .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));
                if (!pacchettoAnnuncio.isAttivo() || pacchettoAnnuncio.getStock() < item.getQuantita())
                    throw new BadRequestException("Pacchetto non disponibile o stock insufficiente");

                pacchettoAnnuncio.setStock(pacchettoAnnuncio.getStock() - item.getQuantita());
                pacchettiRepo.save(pacchettoAnnuncio);

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
        ordine.setDataPagamento(LocalDateTime.now());

        Ordine saved = ordineRepo.save(ordine);

        spedizioniAsyncService.gestisciOrdineAutomatico(saved.getId());

        return OrdineResponse.from(saved);
    }

    @Transactional
    public void eliminaOrdine(Long acquirenteId, Long ordineId) {

        if (acquirenteId == null)
            throw new BadRequestException("acquirenteId mancante");

        if (ordineId == null)
            throw new BadRequestException("ordineId mancante");

        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId))
            throw new BadRequestException("Ordine non appartiene a questo acquirente");

        if (ordine.getStato() == StatoOrdine.PAGATO)
            throw new BadRequestException("Ordine pagato non eliminabile");

        ordineRepo.delete(ordine);
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
}