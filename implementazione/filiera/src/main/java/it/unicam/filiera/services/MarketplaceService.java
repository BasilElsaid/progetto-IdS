package it.unicam.filiera.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.repositories.AnnuncioMarketplaceRepository;

import it.unicam.filiera.controllers.dto.AnnuncioMarketplaceResponse;
import it.unicam.filiera.controllers.dto.CreateAnnuncioMarketplaceRequest;
import it.unicam.filiera.controllers.dto.UpdateAnnuncioMarketplaceRequest;

import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.domain.Prodotto;

import it.unicam.filiera.repositories.AziendaRepository;
import it.unicam.filiera.repositories.ProdottoRepository;

@Service
public class MarketplaceService {

    private final AnnuncioMarketplaceRepository annuncioRepo;
    private final AziendaRepository aziendaRepo;
    private final ProdottoRepository prodottoRepo;

    public MarketplaceService(AnnuncioMarketplaceRepository annuncioRepo,
                              AziendaRepository aziendaRepo,
                              ProdottoRepository prodottoRepo) {
        this.annuncioRepo = annuncioRepo;
        this.aziendaRepo = aziendaRepo;
        this.prodottoRepo = prodottoRepo;
    }

    @Transactional
    public AnnuncioMarketplaceResponse creaAnnuncio(CreateAnnuncioMarketplaceRequest req) {
        Azienda azienda = aziendaRepo.findById(req.aziendaId).orElseThrow();
        Prodotto prodotto = prodottoRepo.findById(req.prodottoId).orElseThrow();

        AnnuncioMarketplace a = new AnnuncioMarketplace();
        a.setAzienda(azienda);
        a.setProdotto(prodotto);
        a.setPrezzo(req.prezzo);
        a.setStock(req.stock);
        a.setAttivo(req.attivo);

        return AnnuncioMarketplaceResponse.from(annuncioRepo.save(a));
    }

    @Transactional(readOnly = true)
    public List<AnnuncioMarketplaceResponse> listaAnnunci(Long aziendaId, String categoria, Boolean attivo) {
        List<AnnuncioMarketplace> all = annuncioRepo.findAll();
        return all.stream()
                .filter(x -> aziendaId == null || x.getAzienda().getId().equals(aziendaId))
                .filter(x -> categoria == null || (x.getProdotto() != null && categoria.equalsIgnoreCase(String.valueOf(x.getProdotto().getCategoria()))))
                .filter(x -> attivo == null || x.isAttivo() == attivo.booleanValue())
                .map(AnnuncioMarketplaceResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AnnuncioMarketplaceResponse getAnnuncio(Long id) {
        return AnnuncioMarketplaceResponse.from(annuncioRepo.findById(id).orElseThrow());
    }

    @Transactional
    public AnnuncioMarketplaceResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        AnnuncioMarketplace a = annuncioRepo.findById(id).orElseThrow();
        if (req.prezzo != null) a.setPrezzo(req.prezzo);
        if (req.stock != null) a.setStock(req.stock);
        if (req.attivo != null) a.setAttivo(req.attivo);
        return AnnuncioMarketplaceResponse.from(annuncioRepo.save(a));
    }

    @Transactional
    public void decrementaStock(Long annuncioId, int quantita) {
        AnnuncioMarketplace a = annuncioRepo.findById(annuncioId).orElseThrow();
        if (!a.isAttivo()) throw new IllegalStateException("Annuncio non attivo");
        if (a.getStock() < quantita) throw new IllegalArgumentException("Stock insufficiente");
        a.setStock(a.getStock() - quantita);
        annuncioRepo.save(a);
    }
}