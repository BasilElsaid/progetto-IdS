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
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplaceProdottiService {

    private final AnnunciProdottiRepository annuncioRepo;
    private final ProdottiRepository prodottoRepo;

    public MarketplaceProdottiService(AnnunciProdottiRepository annuncioRepo,
                                      ProdottiRepository prodottoRepo) {
        this.annuncioRepo = annuncioRepo;
        this.prodottoRepo = prodottoRepo;
    }

    public AnnuncioProdottoResponse creaAnnuncio(CreateAnnuncioProdottoRequest req) {
        if (req == null) {
            throw new BadRequestException("Body mancante");
        }
        if (req.getProdottoId() == null) {
            throw new BadRequestException("prodottoId mancante");
        }

        // prendi l'utente loggato
        var utente = getUtenteLoggato();
        if (!(utente instanceof Azienda azienda)) {
            throw new ForbiddenException("Solo un'azienda può creare annunci");
        }

        Prodotto prodotto = prodottoRepo.findById(req.getProdottoId())
                .orElseThrow(() -> new BadRequestException(
                        "Prodotto non trovato: id=" + req.getProdottoId()
                ));

        if (!prodotto.getProprietario().getId().equals(azienda.getId())) {
            throw new ForbiddenException("Non puoi creare un annuncio per un prodotto che non ti appartiene");
        }

        if (!prodotto.getVendibile()) {
            throw new BadRequestException("Il prodotto non è certificato/approvato e non può essere venduto");
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

    public List<AnnuncioProdottoResponse> listaAnnunci() {

        var utente = getUtenteLoggato();

        List<AnnuncioProdotto> annunci;

        if (utente instanceof Azienda azienda) {
            annunci = annuncioRepo.findByAzienda_Id(azienda.getId());
        } else {
            annunci = annuncioRepo.findAll();
        }

        return annunci.stream()
                .map(AnnuncioProdottoResponse::from)
                .toList();
    }

    @Transactional
    public AnnuncioProdottoResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        if (id == null) {
            throw new BadRequestException("id mancante");
        }
        if (req == null) {
            throw new BadRequestException("Body mancante");
        }

        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Annuncio non trovato: id=" + id
                ));

        var utente = getUtenteLoggato();
        if (!(utente instanceof Azienda azienda)) {
            throw new ForbiddenException("Solo un'azienda può aggiornare un annuncio");
        }

        if (!a.getAzienda().getId().equals(azienda.getId())) {
            throw new ForbiddenException("Non puoi aggiornare un annuncio che non ti appartiene");
        }

        if (req.prezzo() != null) a.setPrezzo(req.prezzo());
        if (req.stock() != null) a.setStock(req.stock());
        if (req.attivo() != null) a.setAttivo(req.attivo());

        return AnnuncioProdottoResponse.from(annuncioRepo.save(a));
    }

    public void eliminaAnnuncio(Long id) {
        AnnuncioProdotto a = annuncioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Annuncio non trovato: id=" + id));

        var utente = getUtenteLoggato();

        // Se è gestore piattaforma → può eliminare tutto
        if (utente.getRuolo().name().equals("GESTORE_PIATTAFORMA")) {
            annuncioRepo.delete(a);
            return;
        }

        if (!(utente instanceof Azienda azienda)) {
            throw new ForbiddenException("Solo un'azienda può eliminare un annuncio");
        }

        if (!a.getAzienda().getId().equals(azienda.getId())) {
            throw new ForbiddenException("Non puoi eliminare un annuncio che non ti appartiene");
        }

        annuncioRepo.delete(a);
    }

    // PER ACQUIRENTI
    public List<AnnuncioProdottoResponse> listaTuttiProdottiResponse() {
        return annuncioRepo.findAll().stream()
                .map(AnnuncioProdottoResponse::from) // usa il metodo statico dal DTO
                .toList();
    }

    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico u)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return (UtenteGenerico) auth.getPrincipal();
    }
}
