package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreateAziendaRequest;
import it.unicam.filiera.controllers.dto.LoginRequest;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.*;
import it.unicam.filiera.repositories.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtenteRepository repo;

    public AuthService(UtenteRepository repo) {
        this.repo = repo;
    }

    public UtenteGenerico register(CreateAziendaRequest req) {
        if (req.getRuolo() == Ruolo.UTENTE_GENERICO) {
            throw new BadRequestException("UtenteGenerico (guest) non può registrarsi: niente login");
        }
        repo.findByUsername(req.getUsername()).ifPresent(u -> {
            throw new BadRequestException("Username già esistente");
        });

        UtenteGenerico u = switch (req.getRuolo()) {
            case ACQUIRENTE -> new Acquirente();
            case PRODUTTORE -> new Produttore();
            case TRASFORMATORE -> new Trasformatore();
            case CURATORE -> new Curatore();
            case ANIMATORE -> new Animatore();
            case GESTORE_PIATTAFORMA -> new GestorePiattaforma();
            case DISTRIBUTORE_TIPICITA -> new DistributoreTipicita();
            default -> throw new BadRequestException("Ruolo non supportato");
        };

        u.setUsername(req.getUsername());
        u.setEmail(req.getEmail());
        u.setPassword(req.getPassword()); // demo: in futuro hash + Spring Security

        return repo.save(u);
    }

    public String login(LoginRequest req) {
        UtenteGenerico u = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        if (u.getPassword() == null || !u.getPassword().equals(req.getPassword())) {
            throw new BadRequestException("Credenziali non valide");
        }

        // token mock per demo
        return "TOKEN_MOCK_" + u.getId() + "_" + u.getRuolo();
    }
}
