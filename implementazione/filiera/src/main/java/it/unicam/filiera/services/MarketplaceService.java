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

import java.util.List;

@Service
public class MarketplaceService {

    private final AnnuncioMarketplaceRepository annunciRepo;
    private final AziendaRepository aziendaRepo;
    private final ProdottoRepository prodottoRepo;

    public MarketplaceService(AnnuncioMarketplaceRepository annunciRepo,
                              AziendaRepository aziendaRepo,
                              ProdottoRepository prodottoRepo) {
        this.annunciRepo = annunciRepo;
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

        return AnnuncioMarketplaceResponse.from(annunciRepo.save(a));
    }

    public List<AnnuncioMarketplaceResponse> listaAnnunci(Long aziendaId, String categoria, Boolean attivo) {
        // versione semplice: filtra in memoria (per demo). Se vuoi, poi facciamo query repository.
        return annunciRepo.findAll().stream()
                .filter(a -> aziendaId == null || a.getAzienda().getId().equals(aziendaId))
                .filter(a -> attivo == null || a.isAttivo() == attivo)
                .filter(a -> categoria == null || (a.getProdotto().getCategoria() != null
                        && a.getProdotto().getCategoria().name().equalsIgnoreCase(categoria)))
                .map(AnnuncioMarketplaceResponse::from)
                .toList();
    }

    public AnnuncioMarketplaceResponse getAnnuncio(Long id) {
        return AnnuncioMarketplaceResponse.from(annunciRepo.findById(id).orElseThrow());
    }

    public AnnuncioMarketplaceResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        AnnuncioMarketplace a = annunciRepo.findById(id).orElseThrow();

        if (req.getPrezzo() != null) a.setPrezzo(req.getPrezzo());
        if (req.getStock() != null) a.setStock(req.getStock());
        if (req.getAttivo() != null) a.setAttivo(req.getAttivo());

        return AnnuncioMarketplaceResponse.from(annunciRepo.save(a));
    }
}
