package it.unicam.filiera.services;

import it.unicam.filiera.dto.response.OrdineResponse;
import it.unicam.filiera.domain.*;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrdiniService {

    private final OrdineRepository ordineRepo;
    private final AcquirenteRepository acquirenteRepo;
    private final AnnuncioProdottoRepository prodottiRepo;
    private final AnnuncioPacchettoRepository pacchettiRepo;

    public OrdiniService(OrdineRepository ordineRepo,
                         AcquirenteRepository acquirenteRepo,
                         AnnuncioProdottoRepository prodottiRepo,
                         AnnuncioPacchettoRepository pacchettiRepo) {
        this.ordineRepo = ordineRepo;
        this.acquirenteRepo = acquirenteRepo;
        this.prodottiRepo = prodottiRepo;
        this.pacchettiRepo = pacchettiRepo;
    }

    @Transactional
    public OrdineResponse creaOrdine(Long acquirenteId, List<ItemRequest> items) {
        if (acquirenteId == null) throw new BadRequestException("acquirenteId mancante");
        if (items == null || items.isEmpty()) throw new BadRequestException("items mancanti");

        Acquirente acquirente = acquirenteRepo.findById(acquirenteId)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        Ordine ordine = new Ordine();
        ordine.setAcquirente(acquirente);
        ordine.setStato(StatoOrdine.IN_CREAZIONE);
        ordine.setDataCreazione(LocalDateTime.now());

        double totale = 0.0;

        for (ItemRequest r : items) {
            if (r.getQuantita() <= 0) throw new BadRequestException("Quantita non valida");

            OrdineItem item = new OrdineItem();
            item.setAnnuncioId(r.getAnnuncioId());
            item.setPacchetto(r.isPacchetto());
            item.setQuantita(r.getQuantita());

            if (r.isPacchetto()) {
                var annuncio = pacchettiRepo.findById(r.getAnnuncioId())
                        .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));
                if (!annuncio.isAttivo() || annuncio.getStock() < r.getQuantita())
                    throw new BadRequestException("Pacchetto non disponibile");
                annuncio.setStock(annuncio.getStock() - r.getQuantita());
                pacchettiRepo.save(annuncio);
                item.setPrezzoUnitario(annuncio.getPrezzo());
            } else {
                var annuncio = prodottiRepo.findById(r.getAnnuncioId())
                        .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));
                if (!annuncio.isAttivo() || annuncio.getStock() < r.getQuantita())
                    throw new BadRequestException("Prodotto non disponibile");
                annuncio.setStock(annuncio.getStock() - r.getQuantita());
                prodottiRepo.save(annuncio);
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
    public OrdineResponse pagaOrdine(Long acquirenteId, Long ordineId, boolean pacchetto, MetodoPagamento metodo) {
        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId))
            throw new BadRequestException("Ordine non appartiene a questo acquirente");

        if (ordine.getStato() != StatoOrdine.IN_ATTESA_PAGAMENTO)
            throw new BadRequestException("Ordine non in attesa pagamento");

        // Controllo disponibilità degli item in base al tipo
        for (OrdineItem item : ordine.getItems()) {
            if (item.isPacchetto() != pacchetto) continue; // ignora gli altri tipi

            if (pacchetto) {
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

        return OrdineResponse.from(ordineRepo.save(ordine));
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

    // DTO interno per richiesta items
    public static class ItemRequest {
        private Long annuncioId;
        private int quantita;
        private boolean pacchetto;

        public Long getAnnuncioId() { return annuncioId; }
        public void setAnnuncioId(Long annuncioId) { this.annuncioId = annuncioId; }
        public int getQuantita() { return quantita; }
        public void setQuantita(int quantita) { this.quantita = quantita; }
        public boolean isPacchetto() { return pacchetto; }
        public void setPacchetto(boolean pacchetto) { this.pacchetto = pacchetto; }
    }
}