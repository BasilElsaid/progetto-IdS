package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CheckoutRequest;
import it.unicam.filiera.domain.*;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.StatoPagamento;
import it.unicam.filiera.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CheckoutService {

    private final AnnuncioMarketplaceRepository annunciRepo;
    private final OrdineRepository ordineRepo;
    private final RigaOrdineMarketplaceRepository righeRepo;
    private final PagamentoOrdineRepository pagamentiRepo;
    private final AcquirenteRepository acquirenteRepo;

    public CheckoutService(AnnuncioMarketplaceRepository annunciRepo,
                           OrdineRepository ordineRepo,
                           RigaOrdineMarketplaceRepository righeRepo,
                           PagamentoOrdineRepository pagamentiRepo,
                           AcquirenteRepository acquirenteRepo) {
        this.annunciRepo = annunciRepo;
        this.ordineRepo = ordineRepo;
        this.righeRepo = righeRepo;
        this.pagamentiRepo = pagamentiRepo;
        this.acquirenteRepo = acquirenteRepo;
    }

    public Ordine creaOrdineDaAnnuncio(CheckoutRequest req) {
        AnnuncioMarketplace annuncio = annunciRepo.findById(req.getAnnuncioId()).orElseThrow();

        if (!annuncio.isAttivo()) throw new IllegalStateException("Annuncio non attivo");
        if (req.getQuantita() <= 0) throw new IllegalArgumentException("Quantità non valida");
        if (annuncio.getStock() < req.getQuantita()) throw new IllegalStateException("Stock insufficiente");

        var acquirente = acquirenteRepo.findById(req.getAcquirenteId()).orElseThrow();

        double prezzoUnitario = annuncio.getPrezzo();
        double totale = prezzoUnitario * req.getQuantita();

        // scala stock
        annuncio.setStock(annuncio.getStock() - req.getQuantita());
        annunciRepo.save(annuncio);

        Ordine ordine = new Ordine();
        ordine.setDataOra(LocalDateTime.now());
        ordine.setStato(StatoOrdine.CREATO);
        ordine.setTotale(totale);
        ordine.setAcquirente(acquirente);
        ordine = ordineRepo.save(ordine);

        RigaOrdineMarketplace riga = new RigaOrdineMarketplace(
                ordine,
                annuncio,
                req.getQuantita(),
                prezzoUnitario
        );
        righeRepo.save(riga);

        return ordine;
    }

    public PagamentoOrdine pagaOrdine(Long ordineId, MetodoPagamento metodo) {
        Ordine ordine = ordineRepo.findById(ordineId).orElseThrow();

        if (ordine.getStato() == StatoOrdine.PAGATO) {
            throw new IllegalStateException("Ordine già pagato");
        }

        PagamentoOrdine p = new PagamentoOrdine(ordine, metodo, ordine.getTotale());
        p.setStato(StatoPagamento.OK);
        p = pagamentiRepo.save(p);

        ordine.setStato(StatoOrdine.PAGATO);
        ordineRepo.save(ordine);

        return p;
    }
}
