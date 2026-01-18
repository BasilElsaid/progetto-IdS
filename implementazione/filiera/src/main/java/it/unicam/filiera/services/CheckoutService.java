package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CheckoutRequest;
import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.StatoPagamento;
import it.unicam.filiera.repositories.AnnuncioMarketplaceRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.PagamentoOrdineRepository;
import it.unicam.filiera.repositories.AcquirenteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutService {

    private final AnnuncioMarketplaceRepository annunciRepo;
    private final OrdineRepository ordiniRepo;
    private final PagamentoOrdineRepository pagamentiRepo;
    private final AcquirenteRepository acquirentiRepo;

    public CheckoutService(AnnuncioMarketplaceRepository annunciRepo,
                           OrdineRepository ordiniRepo,
                           PagamentoOrdineRepository pagamentiRepo,
                           AcquirenteRepository acquirentiRepo) {
        this.annunciRepo = annunciRepo;
        this.ordiniRepo = ordiniRepo;
        this.pagamentiRepo = pagamentiRepo;
        this.acquirentiRepo = acquirentiRepo;
    }

    public Ordine creaOrdineDaAnnuncio(CheckoutRequest req) {
        AnnuncioMarketplace annuncio = annunciRepo.findById(req.getAnnuncioId()).orElseThrow();

        if (req.getQuantita() <= 0) throw new IllegalArgumentException("Quantita non valida");
        if (annuncio.getStock() < req.getQuantita()) throw new IllegalArgumentException("Stock insufficiente");

        var acquirente = acquirentiRepo.findById(req.getAcquirenteId()).orElseThrow();

        double prezzoUnitario = annuncio.getPrezzo();
        double totale = prezzoUnitario * req.getQuantita();

        // scala stock
        annuncio.setStock(annuncio.getStock() - req.getQuantita());
        annunciRepo.save(annuncio);

        Ordine ordine = new Ordine(acquirente);
        ordine.setDataOra(LocalDateTime.now());
        ordine.setStato(StatoOrdine.CREATO);
        ordine.setImportoTotale(totale);

        // semplice: aggiungo prodotto alla lista
        ordine.getProdotti().add(annuncio.getProdotto());

        return ordiniRepo.save(ordine);
    }

    public PagamentoOrdine pagaOrdine(Long ordineId, MetodoPagamento metodo) {
        Ordine ordine = ordiniRepo.findById(ordineId).orElseThrow();
        if (ordine.getStato() == StatoOrdine.PAGATO) {
            throw new IllegalStateException("Ordine gia pagato");
        }

        PagamentoOrdine p = new PagamentoOrdine(ordine, metodo, ordine.getImportoTotale());
        p.setStato(StatoPagamento.OK);
        pagamentiRepo.save(p);

        ordine.setStato(StatoOrdine.PAGATO);
        ordiniRepo.save(ordine);

        return p;
    }
}
