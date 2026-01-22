package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.response.AnnuncioProdottoResponse;
import it.unicam.filiera.controllers.dto.create.CreateAnnuncioProdottoRequest;
import it.unicam.filiera.controllers.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioProdotto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceProdottiService {

    private final AnnuncioProdottoRepository annuncioRepo;
    private final AziendaRepository aziendaRepo;
    private final ProdottoRepository prodottoRepo;

    public MarketplaceProdottiService(AnnuncioProdottoRepository annuncioRepo,
                                      AziendaRepository aziendaRepo,
                                      ProdottoRepository prodottoRepo) {
        this.annuncioRepo = annuncioRepo;
        this.aziendaRepo = aziendaRepo;
        this.prodottoRepo = prodottoRepo;
    }

    public AnnuncioProdottoResponse creaAnnuncio(CreateAnnuncioProdottoRequest req) {
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

        boolean exists = annuncioRepo.findByAziendaAndProdotto(azienda, prodotto).isPresent();
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Annuncio già esistente per azienda " + azienda.getId() + " e prodotto " + prodotto.getId());
        }

        AnnuncioProdotto a = new AnnuncioProdotto();
        a.setAzienda(azienda);
        a.setProdotto(prodotto);
        a.setPrezzo(req.getPrezzo());
        a.setStock(req.getStock());
        a.setAttivo(req.isAttivo());

        return AnnuncioProdottoResponse.from(annuncioRepo.save(a));
    }

    public List<AnnuncioProdottoResponse> listaAnnunci(Long aziendaId, String categoria, Boolean attivo) {
        return annuncioRepo.findAll().stream()
                .filter(a -> aziendaId == null || (a.getAzienda() != null && a.getAzienda().getId().equals(aziendaId)))
                .filter(a -> attivo == null || a.isAttivo() == attivo)
                .filter(a -> categoria == null || categoria.isBlank()
                        || (a.getProdotto() != null
                        && a.getProdotto().getCategoria() != null
                        && a.getProdotto().getCategoria().equalsIgnoreCase(categoria)))
                .map(AnnuncioProdottoResponse::from)
                .collect(Collectors.toList());
    }

    public AnnuncioProdottoResponse getAnnuncio(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id mancante");
        }
        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Annuncio non trovato: id=" + id
                ));
        return AnnuncioProdottoResponse.from(a);
    }

    public AnnuncioProdottoResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id mancante");
        }
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body mancante");
        }

        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Annuncio non trovato: id=" + id
                ));

        if (req.getPrezzo() != null) a.setPrezzo(req.getPrezzo());
        if (req.getStock() != null) a.setStock(req.getStock());
        if (req.getAttivo() != null) a.setAttivo(req.getAttivo());

        return AnnuncioProdottoResponse.from(annuncioRepo.save(a));
    }

    public void eliminaAnnuncio(Long id) {
        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Annuncio non trovato: id=" + id
                ));
        annuncioRepo.delete(a);
    }

    // PER ACQUIRENTI
    public List<AnnuncioProdottoResponse> listaTuttiProdottiResponse() {
        return annuncioRepo.findAll().stream()
                .map(AnnuncioProdottoResponse::from) // usa il metodo statico dal DTO
                .toList();
    }
}
