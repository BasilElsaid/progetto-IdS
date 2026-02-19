package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateAnnuncioPacchettoRequest;
import it.unicam.filiera.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioPacchetto;
import it.unicam.filiera.domain.Pacchetto;
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
public class MarketplacePacchettiService {
    private final PacchettiRepository pacchettoRepo;
    private final AnnunciPacchettiRepository annuncioPacchettoRepo;

    public MarketplacePacchettiService(PacchettiRepository pacchettoRepo,
                                       AnnunciPacchettiRepository annuncioPacchettoRepo) {
        this.pacchettoRepo = pacchettoRepo;
        this.annuncioPacchettoRepo = annuncioPacchettoRepo;
    }

    public AnnuncioPacchettoResponse creaAnnuncioPacchetto(CreateAnnuncioPacchettoRequest req) {
        if (req == null) {
            throw new BadRequestException("Body mancante");
        }
        if (req.getPacchettoId() == null) {
            throw new BadRequestException("pacchettoId mancante");
        }

        // prendi l'utente loggato
        var utente = getUtenteLoggato();
        if (!(utente instanceof Azienda azienda)) {
            throw new ForbiddenException("Solo un'azienda può creare annunci");
        }

        Pacchetto pacchetto = pacchettoRepo.findById(req.getPacchettoId())
                .orElseThrow(() -> new NotFoundException(
                        "Pacchetto non trovato: id=" + req.getPacchettoId()
                ));

        boolean exists = annuncioPacchettoRepo.findByAziendaAndPacchetto(azienda, pacchetto).isPresent();
        if (exists) {
            throw new BadRequestException(
                    "Annuncio già esistente per azienda " + azienda.getId() + " e pacchetto " + pacchetto.getId());
        }

        AnnuncioPacchetto a = new AnnuncioPacchetto();
        a.setAzienda(azienda);
        a.setPacchetto(pacchetto);
        a.setPrezzo(req.getPrezzo());
        a.setStock(req.getStock());
        a.setAttivo(req.isAttivo());

        return AnnuncioPacchettoResponse.from(annuncioPacchettoRepo.save(a));
    }

    @Transactional
    public List<AnnuncioPacchettoResponse> creaAnnunciBatch(List<CreateAnnuncioPacchettoRequest> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            throw new BadRequestException("Lista di annunci vuota");
        }

        return dtos.stream()
                .map(this::creaAnnuncioPacchetto)
                .collect(Collectors.toList());
    }

    public List<AnnuncioPacchettoResponse> listaAnnunci() {

        var utente = getUtenteLoggato();
        List<AnnuncioPacchetto> annunci;

        if (utente instanceof Azienda azienda) {
            annunci = annuncioPacchettoRepo.findByAzienda_Id(azienda.getId());
        } else {
            annunci = annuncioPacchettoRepo.findAll();
        }

        return annunci.stream()
                .map(AnnuncioPacchettoResponse::from)
                .toList();
    }

    @Transactional
    public AnnuncioPacchettoResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {

        if (id == null) {
            throw new BadRequestException("id mancante");
        }
        if (req == null) {
            throw new BadRequestException("Body mancante");
        }

        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
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

        return AnnuncioPacchettoResponse.from(annuncioPacchettoRepo.save(a));
    }

    @Transactional
    public void eliminaAnnuncioPacchetto(Long id) {

        if (id == null) {
            throw new BadRequestException("id mancante");
        }

        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Annuncio non trovato: id=" + id
                ));

        var utente = getUtenteLoggato();

        // Gestore può eliminare tutto
        if (utente.getRuolo().name().equals("GESTORE_PIATTAFORMA")) {
            annuncioPacchettoRepo.delete(a);
            return;
        }

        if (!(utente instanceof Azienda azienda)) {
            throw new ForbiddenException("Solo un'azienda può eliminare un annuncio");
        }

        if (!a.getAzienda().getId().equals(azienda.getId())) {
            throw new ForbiddenException("Non puoi eliminare un annuncio che non ti appartiene");
        }

        annuncioPacchettoRepo.delete(a);
    }

    // PER ACQUIRENTI
    public List<AnnuncioPacchettoResponse> listaTuttiProdottiResponse() {
        return annuncioPacchettoRepo.findAll().stream()
                .map(AnnuncioPacchettoResponse::from)
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
