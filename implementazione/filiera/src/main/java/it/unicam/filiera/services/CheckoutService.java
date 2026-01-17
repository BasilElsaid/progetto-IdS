package it.unicam.filiera.services;
import it.unicam.filiera.enums.StatoPagamento;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.domain.RigaOrdineMarketplace;

import it.unicam.filiera.repositories.AnnuncioMarketplaceRepository;
import it.unicam.filiera.repositories.PagamentoOrdineRepository;
import it.unicam.filiera.repositories.RigaOrdineMarketplaceRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.AcquirenteRepository;

import it.unicam.filiera.controllers.dto.CheckoutResponse;
import it.unicam.filiera.controllers.dto.PagaOrdineRequest;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.enums.StatoOrdine;

@Service
public class CheckoutService {

    private final AnnuncioMarketplaceRepository annuncioRepo;
    private final OrdineRepository ordineRepo;
    private final RigaOrdineMarketplaceRepository rigaRepo;
    private final PagamentoOrdineRepository pagamentoRepo;
    private final AcquirenteRepository acquirenteRepo;

    public CheckoutService(AnnuncioMarketplaceRepository annuncioRepo,
                           OrdineRepository ordineRepo,
                           RigaOrdineMarketplaceRepository rigaRepo,
                           PagamentoOrdineRepository pagamentoRepo,
                           AcquirenteRepository acquirenteRepo) {
        this.annuncioRepo = annuncioRepo;
        this.ordineRepo = ordineRepo;
        this.rigaRepo = rigaRepo;
        this.pagamentoRepo = pagamentoRepo;
        this.acquirenteRepo = acquirenteRepo;
    }

    @Transactional
    public Ordine creaOrdineDaAnnuncio(CheckoutRequest req) {
        AnnuncioMarketplace annuncio = annuncioRepo.findById(req.annuncioId).orElseThrow();
        if (!annuncio.isAttivo()) throw new IllegalStateException("Annuncio non attivo");
        if (annuncio.getStock() < req.quantita) throw new IllegalArgumentException("Stock insufficiente");

        Acquirente acq = acquirenteRepo.findById(req.acquirenteId).orElseThrow();

        double totale = annuncio.getPrezzo() * req.quantita;

        Ordine ordine = new Ordine();
        ordine.setTotale(totale);
        ordine.setAcquirente(acq);
        ordine.setStato(StatoOrdine.CREATO);
        ordine = ordineRepo.save(ordine);

        RigaOrdineMarketplace r = new RigaOrdineMarketplace();
        r.setOrdine(ordine);
        r.setAnnuncio(annuncio);
        r.setQuantita(req.quantita);
        r.setPrezzoUnitarioAlMomento(annuncio.getPrezzo());
        r.setSubtotale(totale);
        rigaRepo.save(r);

        annuncio.setStock(annuncio.getStock() - req.quantita);
        annuncioRepo.save(annuncio);

        return ordine;
    }

    @Transactional
    public PagamentoOrdine pagaOrdine(Long ordineId, PagaOrdineRequest req) {
        Ordine ordine = ordineRepo.findById(ordineId).orElseThrow();
        if (ordine.getStato() == StatoOrdine.PAGATO) {
            throw new IllegalStateException("Ordine già pagato");
        }

        PagamentoOrdine p = new PagamentoOrdine();
        p.setOrdine(ordine);
        p.setMetodo(req.metodo);
        p.setStato(StatoPagamento.OK);
        p.setImporto(ordine.getTotale());
        p.setTransactionId("TX-" + UUID.randomUUID());

        pagamentoRepo.save(p);

        ordine.setStato(StatoOrdine.PAGATO);
        ordineRepo.save(ordine);

        return p;
    }
}