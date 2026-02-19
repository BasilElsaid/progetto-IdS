package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.dto.update.UpdatePersonaleRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.*;
import it.unicam.filiera.repositories.UtentiRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaleService {

    private final UtentiRepository utentiRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonaleService(UtentiRepository utentiRepository, PasswordEncoder passwordEncoder) {
        this.utentiRepository = utentiRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteResponse creaPersonale(CreatePersonaleRequest request) {
        // Controlla se già esiste un utente con lo stesso ruolo
        if (utentiRepository.existsByRuolo(request.ruolo())) {
            throw new BadRequestException(
                    switch (request.ruolo()) {
                        case CURATORE -> "Curatore già presente";
                        case ANIMATORE -> "Animatore già presente";
                        case GESTORE_PIATTAFORMA -> "Gestore piattaforma già presente";
                        default -> "Ruolo non valido per il personale";
                    }
            );
        }

        Personale personale;
        switch (request.ruolo()) {
            case CURATORE -> personale = new Curatore();
            case ANIMATORE -> personale = new Animatore();
            case GESTORE_PIATTAFORMA -> personale = new GestorePiattaforma();
            default -> throw new BadRequestException("Ruolo non valido per il personale");
        }

        personale.setUsername(request.username());
        personale.setEmail(request.email());
        personale.setPassword(passwordEncoder.encode(request.password()));
        personale.setNome(request.nome());
        personale.setCognome(request.cognome());
        personale.setTelefono(request.telefono());

        return UtenteResponse.from(utentiRepository.save(personale));
    }

    public List<UtenteResponse> listaPersonale() {
        UtenteGenerico u = getUtenteLoggato();

        if (u instanceof GestorePiattaforma) {
            return utentiRepository.findAll().stream()
                    .filter(p -> p instanceof Personale)
                    .map(p -> UtenteResponse.from((Personale) p))
                    .toList();
        }

        if (u instanceof Personale personale) {
            return List.of(UtenteResponse.from(personale));
        }

        throw new ForbiddenException("Non puoi vedere il personale");
    }

    public void eliminaPersonale(Long id) {
        UtenteGenerico u = getUtenteLoggato();
        UtenteGenerico target = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Personale non trovato"));

        if (!(target instanceof Personale personale)) {
            throw new BadRequestException("L'utente non è personale");
        }

        if (!u.getId().equals(personale.getId()) && !(u instanceof GestorePiattaforma)) {
            throw new ForbiddenException("Non puoi eliminare questo personale");
        }

        utentiRepository.delete(personale);
    }

    public UtenteResponse patchPersonale(Long id, UpdatePersonaleRequest request) {
        UtenteGenerico u = getUtenteLoggato();
        UtenteGenerico target = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Personale non trovato"));

        if (!(target instanceof Personale personale)) {
            throw new BadRequestException("L'utente non è personale");
        }

        // Controllo proprietà o ruolo gestore
        if (!u.getId().equals(personale.getId())) {
            throw new ForbiddenException("Non puoi modificare questo personale");
        }

        if (request.email() != null) personale.setEmail(request.email());
        if (request.password() != null) personale.setPassword(passwordEncoder.encode(request.password()));
        if (request.nome() != null) personale.setNome(request.nome());
        if (request.cognome() != null) personale.setCognome(request.cognome());
        if (request.telefono() != null) personale.setTelefono(request.telefono());

        return UtenteResponse.from(utentiRepository.save(personale));
    }

    // =================== Helper ===================
    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return user;
    }
}