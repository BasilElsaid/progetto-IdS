package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoStatoRequest;
import it.unicam.filiera.domain.OffertaPacchetto;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.DistributoreTipicitaRepository;
import it.unicam.filiera.repositories.OffertaPacchettoRepository;
import it.unicam.filiera.repositories.PacchettoRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistribuzioneService {

    private final DistributoreTipicitaRepository distributoreRepo;
    private final PacchettoRepository pacchettoRepo;
    private final OffertaPacchettoRepository offertaRepo;

    public DistribuzioneService(DistributoreTipicitaRepository distributoreRepo,
                                PacchettoRepository pacchettoRepo,
                                OffertaPacchettoRepository offertaRepo) {
        this.distributoreRepo = distributoreRepo;
        this.pacchettoRepo = pacchettoRepo;
        this.offertaRepo = offertaRepo;
    }

    public OffertaPacchettoResponse creaOfferta(CreateOffertaPacchettoRequest req) {

        UtenteGenerico utente = getUtenteLoggato();

        DistributoreTipicita distributore;

        if (isDistributore(utente)) {
            distributore = (DistributoreTipicita) utente;
        } else if (isGestorePiattaforma(utente)) {
            throw new BadRequestException("Gestore piattaforma deve specificare il distributore separatamente");
            // oppure possiamo decidere di creare offerte per un distributore fisso
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        Pacchetto pacchetto = pacchettoRepo.findById(req.getPacchettoId())
                .orElseThrow(() -> new NotFoundException("Pacchetto non trovato"));

        OffertaPacchetto offerta = new OffertaPacchetto(
                distributore,
                pacchetto,
                req.getPrezzoVendita(),
                req.getDisponibilita(),
                req.isAttiva()
        );

        return OffertaPacchettoResponse.from(offertaRepo.save(offerta));
    }

    public List<OffertaPacchettoResponse> listaOfferte() {
        UtenteGenerico utente = getUtenteLoggato();

        if (isDistributore(utente)) {
            return offertaRepo.findByDistributoreId(((DistributoreTipicita) utente).getId())
                    .stream().map(OffertaPacchettoResponse::from).collect(Collectors.toList());
        } else if (isGestorePiattaforma(utente)) {
            return offertaRepo.findAll()
                    .stream().map(OffertaPacchettoResponse::from).collect(Collectors.toList());
        } else {
            throw new ForbiddenException("Ruolo non autorizzato");
        }
    }

    public OffertaPacchettoResponse aggiornaStatoStock(Long offertaId, UpdateOffertaPacchettoStatoRequest req) {
        UtenteGenerico utente = getUtenteLoggato();
        OffertaPacchetto offerta = offertaRepo.findById(offertaId)
                .orElseThrow(() -> new NotFoundException("Offerta non trovata"));

        // Controllo accesso: distributore può modificare solo le proprie offerte
        if (isDistributore(utente) &&
                !offerta.getDistributore().getId().equals(((DistributoreTipicita) utente).getId())) {
            throw new ForbiddenException("Non puoi modificare offerte di altri distributori");
        }

        // Aggiornamento campi se presenti
        if (req.getAttiva() != null) {
            offerta.setAttiva(req.getAttiva());
        }
        if (req.getDisponibilita() != null) {
            offerta.setDisponibilita(req.getDisponibilita());
        }

        return OffertaPacchettoResponse.from(offertaRepo.save(offerta));
    }

    // ================= HELPERS =================
    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico u)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return u;
    }

    private boolean isGestorePiattaforma(UtenteGenerico u) {
        return u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA;
    }

    private boolean isDistributore(UtenteGenerico u) {
        return u instanceof DistributoreTipicita;
    }
}
