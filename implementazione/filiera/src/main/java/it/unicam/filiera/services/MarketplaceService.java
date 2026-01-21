package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.response.AnnuncioMarketplaceResponse;
import it.unicam.filiera.controllers.dto.create.CreateAnnuncioMarketplaceRequest;
import it.unicam.filiera.controllers.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioMarketplace;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.AnnuncioMarketplaceRepository;
import it.unicam.filiera.repositories.AziendaRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body mancante");
        }
        if (req.getAziendaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "aziendaId mancante");
        }
        if (req.getProdottoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prodottoId mancante");
        }

        var azienda = aziendaRepo.findById(req.getAziendaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Azienda non trovata: id=" + req.getAziendaId()
                ));

        Prodotto prodotto = prodottoRepo.findById(req.getProdottoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prodotto non trovato: id=" + req.getProdottoId()
                ));

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
                .filter(a -> aziendaId == null || (a.getAzienda() != null && a.getAzienda().getId().equals(aziendaId)))
                .filter(a -> attivo == null || a.isAttivo() == attivo)
                .filter(a -> categoria == null || categoria.isBlank()
                        || (a.getProdotto() != null
                        && a.getProdotto().getCategoria() != null
                        && a.getProdotto().getCategoria().equalsIgnoreCase(categoria)))
                .map(AnnuncioMarketplaceResponse::from)
                .collect(Collectors.toList());
    }

    public AnnuncioMarketplaceResponse getAnnuncio(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id mancante");
        }
        AnnuncioMarketplace a = annuncioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Annuncio non trovato: id=" + id
                ));
        return AnnuncioMarketplaceResponse.from(a);
    }

    public AnnuncioMarketplaceResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id mancante");
        }
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body mancante");
        }

        AnnuncioMarketplace a = annuncioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Annuncio non trovato: id=" + id
                ));

        if (req.getPrezzo() != null) a.setPrezzo(req.getPrezzo());
        if (req.getStock() != null) a.setStock(req.getStock());
        if (req.getAttivo() != null) a.setAttivo(req.getAttivo());

        return AnnuncioMarketplaceResponse.from(annuncioRepo.save(a));
    }
}
