package it.unicam.filiera.services;

import it.unicam.filiera.domain.*;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.StatoPagamento;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.repositories.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CarrelloService {

    private static final int MINUTI_SCADENZA = 20;

    private final CarrelloRepository carrelloRepo;
    private final CarrelloItemRepository itemRepo;
    private final AnnuncioMarketplaceRepository annunciRepo;
    private final AcquirenteRepository acquirentiRepo;
    private final OrdineRepository ordiniRepo;
    private final PagamentoOrdineRepository pagamentiRepo;

    public CarrelloService(CarrelloRepository carrelloRepo,
                           CarrelloItemRepository itemRepo,
                           AnnuncioMarketplaceRepository annunciRepo,
                           AcquirenteRepository acquirentiRepo,
                           OrdineRepository ordiniRepo,
                           PagamentoOrdineRepository pagamentiRepo) {
        this.carrelloRepo = carrelloRepo;
        this.itemRepo = itemRepo;
        this.annunciRepo = annunciRepo;
        this.acquirentiRepo = acquirentiRepo;
        this.ordiniRepo = ordiniRepo;
        this.pagamentiRepo = pagamentiRepo;
    }

    @Transactional
    public Carrello getOrCreate(Long acquirenteId) {
        return carrelloRepo.findByAcquirenteId(acquirenteId)
                .orElseGet(() -> {
                    var a = acquirentiRepo.findById(acquirenteId)
                            .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));
                    return carrelloRepo.save(new Carrello(a));
                });
    }

    @Transactional
    public Carrello aggiungiItem(Long acquirenteId, Long annuncioId, int quantita) {
        if (quantita <= 0) throw new BadRequestException("Quantita non valida");

        Carrello carrello = getOrCreate(acquirenteId);

        AnnuncioMarketplace annuncio = annunciRepo.findByIdForUpdate(annuncioId)
                .orElseThrow(() -> new NotFoundException("Annuncio non trovato"));

        if (!annuncio.isAttivo()) throw new BadRequestException("Annuncio non attivo");
        if (annuncio.getStock() < quantita) throw new BadRequestException("Stock insufficiente");

        annuncio.setStock(annuncio.getStock() - quantita);
        annunciRepo.save(annuncio);

        LocalDateTime scade = LocalDateTime.now().plusMinutes(MINUTI_SCADENZA);
        CarrelloItem item = new CarrelloItem(annuncio, quantita, annuncio.getPrezzo(), scade);

        carrello.addItem(item);
        return carrelloRepo.save(carrello);
    }

    @Transactional
    public Carrello rimuoviItem(Long acquirenteId, Long itemId) {
        Carrello carrello = getOrCreate(acquirenteId);

        CarrelloItem item = itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item non trovato"));

        if (!item.getCarrello().getId().equals(carrello.getId())) {
            throw new BadRequestException("Item non appartiene a questo carrello");
        }

        AnnuncioMarketplace annuncio = annunciRepo.findByIdForUpdate(item.getAnnuncio().getId())
                .orElseThrow(() -> new NotFoundException("Annuncio non trovato"));

        annuncio.setStock(annuncio.getStock() + item.getQuantita());
        annunciRepo.save(annuncio);

        carrello.getItems().remove(item);
        carrelloRepo.save(carrello);
        return carrello;
    }

    @Transactional
    public Ordine checkout(Long acquirenteId) {
        Carrello carrello = getOrCreate(acquirenteId);

        if (carrello.getItems().isEmpty()) {
            throw new BadRequestException("Carrello vuoto");
        }

        LocalDateTime now = LocalDateTime.now();
        boolean scaduto = carrello.getItems().stream().anyMatch(i -> i.isScaduto(now));
        if (scaduto) throw new BadRequestException("Carrello contiene item scaduti");

        Ordine ordine = new Ordine(carrello.getAcquirente());
        ordine.setDataOra(now);
        ordine.setStato(StatoOrdine.IN_ATTESA_PAGAMENTO);

        double totale = 0.0;
        for (CarrelloItem ci : carrello.getItems()) {
            OrdineItemMarketplace oi = new OrdineItemMarketplace(ci.getAnnuncio(), ci.getQuantita(), ci.getPrezzoUnitario());
            ordine.aggiungiItemMarketplace(oi);
            totale += oi.getTotaleRiga();
        }
        ordine.setImportoTotale(totale);

        // svuota carrello
        carrello.getItems().clear();
        carrelloRepo.save(carrello);

        return ordiniRepo.save(ordine);
    }

    @Transactional
    public PagamentoOrdine pagaOrdine(Long acquirenteId, Long ordineId, MetodoPagamento metodo) {
        if (ordineId == null) throw new BadRequestException("ordineId mancante");
        if (metodo == null) throw new BadRequestException("metodo mancante");

        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId)) {
            throw new BadRequestException("Ordine non appartiene a questo acquirente");
        }

        if (ordine.getStato() != StatoOrdine.IN_ATTESA_PAGAMENTO) {
            throw new BadRequestException("Ordine non in attesa pagamento");
        }

        PagamentoOrdine p = new PagamentoOrdine(ordine, metodo, ordine.getImportoTotale());
        p.setStato(StatoPagamento.OK);
        p.setPagatoIl(LocalDateTime.now());
        pagamentiRepo.save(p);

        ordine.setStato(StatoOrdine.PAGATO);
        ordiniRepo.save(ordine);

        return p;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void liberaStockScaduti() {
        LocalDateTime now = LocalDateTime.now();
        var scaduti = itemRepo.findByScadeIlBefore(now);

        for (CarrelloItem item : scaduti) {
            AnnuncioMarketplace annuncio = annunciRepo.findByIdForUpdate(item.getAnnuncio().getId()).orElse(null);
            if (annuncio != null) {
                annuncio.setStock(annuncio.getStock() + item.getQuantita());
                annunciRepo.save(annuncio);
            }
            itemRepo.delete(item);
        }
    }
}
