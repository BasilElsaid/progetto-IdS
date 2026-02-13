package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.dto.update.UpdatePersonaleRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.*;
import it.unicam.filiera.repositories.UtentiRepository;
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
        return utentiRepository.findAll().stream()
                .filter(u -> u instanceof Personale)
                .map(u -> UtenteResponse.from((Personale) u))
                .toList();
    }

    public UtenteResponse getPersonale(Long id) {
        UtenteGenerico u = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Personale non trovato"));

        if (!(u instanceof Personale personale)) {
            throw new BadRequestException("L'utente non è personale");
        }

        return UtenteResponse.from(personale);
    }

    public void eliminaPersonale(Long id) {
        UtenteGenerico u = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Personale non trovato"));
        if (!(u instanceof Personale)) {
            throw new BadRequestException("L'utente non è personale");
        }
        utentiRepository.delete(u);
    }

    public UtenteResponse patchPersonale(Long id, UpdatePersonaleRequest request) {
        UtenteGenerico u = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Personale non trovato"));

        if (!(u instanceof Personale personale)) {
            throw new BadRequestException("L'utente non è personale");
        }

        if (request.email() != null) personale.setEmail(request.email());
        if (request.password() != null) personale.setPassword(passwordEncoder.encode(request.password()));
        if (request.nome() != null) personale.setNome(request.nome());
        if (request.cognome() != null) personale.setCognome(request.cognome());
        if (request.telefono() != null) personale.setTelefono(request.telefono());

        return UtenteResponse.from(utentiRepository.save(personale));
    }

    // =================== Helper ===================
    private void updateUtente(Object utente, UpdatePersonaleRequest request) {
        if (utente instanceof Curatore c) {
            if (request.email() != null) c.setEmail(request.email());
            if (request.password() != null) c.setPassword(passwordEncoder.encode(request.password()));
            if (request.nome() != null) c.setNome(request.nome());
            if (request.cognome() != null) c.setCognome(request.cognome());
            if (request.telefono() != null) c.setTelefono(request.telefono());

        } else if (utente instanceof Animatore a) {
            if (request.email() != null) a.setEmail(request.email());
            if (request.password() != null) a.setPassword(passwordEncoder.encode(request.password()));
            if (request.nome() != null) a.setNome(request.nome());
            if (request.cognome() != null) a.setCognome(request.cognome());
            if (request.telefono() != null) a.setTelefono(request.telefono());

        } else if (utente instanceof GestorePiattaforma g) {
            if (request.email() != null) g.setEmail(request.email());
            if (request.password() != null) g.setPassword(passwordEncoder.encode(request.password()));
            if (request.nome() != null) g.setNome(request.nome());
            if (request.cognome() != null) g.setCognome(request.cognome());
            if (request.telefono() != null) g.setTelefono(request.telefono());
        }
    }
}