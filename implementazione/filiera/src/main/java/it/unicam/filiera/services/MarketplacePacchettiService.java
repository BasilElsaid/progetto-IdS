package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateAnnuncioPacchettoRequest;
import it.unicam.filiera.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.domain.AnnuncioPacchetto;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new BadRequestException( "Body mancante");
        }
        if (req.getAziendaId() == null) {
            throw new BadRequestException( "aziendaId mancante");
        }
        if (req.getPacchettoId() == null) {
            throw new BadRequestException( "pacchettoId mancante");
        }

        var azienda = aziendaRepo.findById(req.getAziendaId())
                .orElseThrow(() -> new NotFoundException(
                        "Azienda non trovata: id=" + req.getAziendaId()
                ));

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

    public List<AnnuncioPacchettoResponse> listaAnnunci(Long aziendaId, Boolean attivo) {
        return annuncioPacchettoRepo.findAll().stream()
                .filter(a -> aziendaId == null || (a.getAzienda() != null && a.getAzienda().getId().equals(aziendaId)))
                .filter(a -> attivo == null || a.isAttivo() == attivo)
                .map(AnnuncioPacchettoResponse::from)
                .collect(Collectors.toList());
    }

    public AnnuncioPacchettoResponse getAnnuncio(Long id) {
        if (id == null) {
            throw new BadRequestException( "id mancante");
        }
        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Annuncio non trovato: id=" + id
                ));
        return AnnuncioPacchettoResponse.from(a);
    }

    public AnnuncioPacchettoResponse aggiornaAnnuncio(Long id, UpdateAnnuncioMarketplaceRequest req) {
        if (id == null) {
            throw new BadRequestException( "id mancante");
        }
        if (req == null) {
            throw new BadRequestException( "Body mancante");
        }

        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Annuncio non trovato: id=" + id
                ));

        if (req.prezzo() != null) a.setPrezzo(req.prezzo());
        if (req.stock() != null) a.setStock(req.stock());
        if (req.attivo() != null) a.setAttivo(req.attivo());

        return AnnuncioPacchettoResponse.from(annuncioPacchettoRepo.save(a));
    }

    public void eliminaAnnuncioPacchetto(Long id) {
        AnnuncioPacchetto a = annuncioPacchettoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException( "Annuncio non trovato: id=" + id
                ));
        annuncioPacchettoRepo.delete(a);
    }

    // PER ACQUIRENTI
    public List<AnnuncioPacchettoResponse> listaTuttiProdottiResponse() {
        return annuncioPacchettoRepo.findAll().stream()
                .map(AnnuncioPacchettoResponse::from)
                .toList();
    }
}
