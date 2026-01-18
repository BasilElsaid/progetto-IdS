package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.AnnuncioMarketplaceResponse;
import it.unicam.filiera.controllers.dto.CreateAnnuncioMarketplaceRequest;
import it.unicam.filiera.controllers.dto.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.AnnuncioMarketplaceRepository;
import it.unicam.filiera.repositories.AziendaRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public AnnuncioMarketplaceResponse creaAnnuncio(CreateAnnuncioMarketplaceRequest req) {
        var azienda = aziendaRepo.findById(req.getAziendaId()).orElseThrow();
        Prodotto prodotto = prodottoRepo.findById(req.getProdottoId()).orElseThrow();

        AnnuncioMarketplace a = new AnnuncioMarketplace();
        a.setAzienda(azienda);
        a.setProdotto(prodotto);
        a.setPrezzo(req.getPrezzo());
        a.setStock(req.getStock());
        a.setAttivo(req.isAttivo());

        return AnnuncioMarketplaceResponse.from(annuncioRepo.save(a));
    }

    public List<AnnuncioMarketplaceResponse> listaAnnunci(Long aziendaId, String categoria, Boolean attivo) {
        return annuncioRepo.findAll().stream()
                .filter(a -> aziendaId == null || a.getAzienda().getId().equals(aziendaId))
                .filter(a -> attivo == null || a.isAttivo() == attivo)
                .filter(a -> categoria == null || categoria.isBlank()
                        || (a.getProdotto().getCategoria() != null
                        && a.getProdotto().getCategoria().equalsIgnoreCase(categoria)))
                .map(AnnuncioMarketplaceResponse::from)
                .collect(Collectors.toList());
    }

    public AnnuncioMarketplaceResponse getAnnuncio(Long id) {
        return AnnuncioMarketplaceResponse.from(annuncioRepo.findById(id).orElseThrow());
    }

    public AnnuncioMarketplaceResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        AnnuncioMarketplace a = annuncioRepo.findById(id).orElseThrow();

        if (req.getPrezzo() != null) a.setPrezzo(req.getPrezzo());
        if (req.getStock() != null) a.setStock(req.getStock());
        if (req.getAttivo() != null) a.setAttivo(req.getAttivo());

        return AnnuncioMarketplaceResponse.from(annuncioRepo.save(a));
    }
}
