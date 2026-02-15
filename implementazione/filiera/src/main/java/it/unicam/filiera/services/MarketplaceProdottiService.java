package it.unicam.filiera.services;

import it.unicam.filiera.dto.response.AnnuncioProdottoResponse;
import it.unicam.filiera.dto.create.CreateAnnuncioProdottoRequest;
import it.unicam.filiera.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioProdotto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceProdottiService {

    private final AnnunciProdottiRepository annuncioRepo;
    private final UtentiRepository utentiRepo;
    private final ProdottiRepository prodottoRepo;

    public MarketplaceProdottiService(AnnunciProdottiRepository annuncioRepo,
                                      UtentiRepository utentiRepo,
                                      ProdottiRepository prodottoRepo) {
        this.annuncioRepo = annuncioRepo;
        this.utentiRepo = utentiRepo;
        this.prodottoRepo = prodottoRepo;
    }

    public AnnuncioProdottoResponse creaAnnuncio(CreateAnnuncioProdottoRequest req) {
        if (req == null) {
            throw new BadRequestException( "Body mancante");
        }
        if (req.getAziendaId() == null) {
            throw new BadRequestException("aziendaId mancante");
        }
        if (req.getProdottoId() == null) {
            throw new BadRequestException( "prodottoId mancante");
        }

        var utente = utentiRepo.findById(req.getAziendaId())
                .orElseThrow(() -> new NotFoundException(
                        "Azienda non trovata: id=" + req.getAziendaId()
                ));

        if (!(utente instanceof Azienda azienda)) {
            throw new BadRequestException("L'utente selezionato non è un'azienda");
        }

        Prodotto prodotto = prodottoRepo.findById(req.getProdottoId())
                .orElseThrow(() -> new BadRequestException(
                        "Prodotto non trovato: id=" + req.getProdottoId()
                ));

        if (!prodotto.getProprietario().getId().equals(azienda.getId())) {
            throw new ForbiddenException(
                    "Non puoi creare un annuncio per un prodotto che non ti appartiene"
            );
        }

        if (!prodotto.getVendibile()) {
            throw new BadRequestException(
                    "Il prodotto non è certificato/approvato e non può essere trasformato"
            );
        }

        boolean exists = annuncioRepo.findByAziendaAndProdotto(azienda, prodotto).isPresent();
        if (exists) {
            throw new BadRequestException(
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

    @Transactional
    public List<AnnuncioProdottoResponse> creaAnnunciBatch(List<CreateAnnuncioProdottoRequest> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            throw new BadRequestException("Lista di annunci vuota");
        }

        return dtos.stream()
                .map(this::creaAnnuncio)
                .collect(Collectors.toList());
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
            throw new BadRequestException( "id mancante");
        }
        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Annuncio non trovato: id=" + id
                ));
        return AnnuncioProdottoResponse.from(a);
    }

    public AnnuncioProdottoResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        if (id == null) {
            throw new BadRequestException( "id mancante");
        }
        if (req == null) {
            throw new BadRequestException( "Body mancante");
        }

        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Annuncio non trovato: id=" + id
                ));

        if (req.prezzo() != null) a.setPrezzo(req.prezzo());
        if (req.stock() != null) a.setStock(req.stock());
        if (req.attivo() != null) a.setAttivo(req.attivo());

        return AnnuncioProdottoResponse.from(annuncioRepo.save(a));
    }

    public void eliminaAnnuncio(Long id) {
        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Annuncio non trovato: id=" + id
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
