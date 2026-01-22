package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.create.CreateAnnuncioPacchettoRequest;
import it.unicam.filiera.controllers.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.controllers.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioPacchetto;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarketplacePacchettiService {
    private final AziendaRepository aziendaRepo;
    private final PacchettoRepository pacchettoRepo;
    private final AnnuncioPacchettoRepository annuncioPacchettoRepo;

    public MarketplacePacchettiService(AziendaRepository aziendaRepo,
                                       PacchettoRepository pacchettoRepo,
                                       AnnuncioPacchettoRepository annuncioPacchettoRepo) {
        this.aziendaRepo = aziendaRepo;
        this.pacchettoRepo = pacchettoRepo;
        this.annuncioPacchettoRepo = annuncioPacchettoRepo;
    }

    public AnnuncioPacchettoResponse creaAnnuncioPacchetto(CreateAnnuncioPacchettoRequest req) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body mancante");
        }
        if (req.getAziendaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "aziendaId mancante");
        }
        if (req.getPacchettoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "pacchettoId mancante");
        }

        var azienda = aziendaRepo.findById(req.getAziendaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Azienda non trovata: id=" + req.getAziendaId()
                ));

        Pacchetto pacchetto = pacchettoRepo.findById(req.getPacchettoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pacchetto non trovato: id=" + req.getPacchettoId()
                ));

        boolean exists = annuncioPacchettoRepo.findByAziendaAndPacchetto(azienda, pacchetto).isPresent();
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
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

    public List<AnnuncioPacchettoResponse> listaAnnunci(Long aziendaId, Boolean attivo) {
        return annuncioPacchettoRepo.findAll().stream()
                .filter(a -> aziendaId == null || (a.getAzienda() != null && a.getAzienda().getId().equals(aziendaId)))
                .filter(a -> attivo == null || a.isAttivo() == attivo)
                .map(AnnuncioPacchettoResponse::from)
                .collect(Collectors.toList());
    }

    public AnnuncioPacchettoResponse getAnnuncio(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id mancante");
        }
        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Annuncio non trovato: id=" + id
                ));
        return AnnuncioPacchettoResponse.from(a);
    }

    public AnnuncioPacchettoResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id mancante");
        }
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body mancante");
        }

        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Annuncio non trovato: id=" + id
                ));

        if (req.getPrezzo() != null) a.setPrezzo(req.getPrezzo());
        if (req.getStock() != null) a.setStock(req.getStock());
        if (req.getAttivo() != null) a.setAttivo(req.getAttivo());

        return AnnuncioPacchettoResponse.from(annuncioPacchettoRepo.save(a));
    }

    public void eliminaAnnuncioPacchetto(Long id) {
        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Annuncio non trovato: id=" + id
                ));
        annuncioPacchettoRepo.delete(a);
    }
}
