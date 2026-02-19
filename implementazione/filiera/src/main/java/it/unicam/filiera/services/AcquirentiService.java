package it.unicam.filiera.services;

import it.unicam.filiera.builder.AcquirenteBuilder;
import it.unicam.filiera.dto.create.CreateAcquirenteRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.dto.update.UpdateAcquirenteRequest;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.UtentiRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcquirentiService {

    private final UtentiRepository utentiRepository;
    private final PasswordEncoder passwordEncoder;

    public AcquirentiService(UtentiRepository utentiRepository, PasswordEncoder passwordEncoder) {
        this.utentiRepository = utentiRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteResponse creaAcquirente(CreateAcquirenteRequest request) {
        if (request == null) throw new BadRequestException("Request mancante");

        if (utentiRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username già utilizzato");
        }
        if (utentiRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email già utilizzata");
        }

        Acquirente acquirente = (Acquirente) new AcquirenteBuilder()
                .setUsername(request.username())
                .setPassword(passwordEncoder.encode(request.password()))
                .setEmail(request.email())
                .build();

        Acquirente saved = utentiRepository.save(acquirente);

        return UtenteResponse.from(saved);
    }

    public List<UtenteResponse> listaAcquirenti() {
        UtenteGenerico u = getUtenteLoggato();

        if (u instanceof GestorePiattaforma) {
            return utentiRepository.findAll().stream()
                    .filter(a -> a instanceof Acquirente)
                    .map(a -> UtenteResponse.from((Acquirente) a))
                    .toList();
        }

        if (u instanceof Acquirente acquirente) {
            return List.of(UtenteResponse.from(acquirente));
        }

        throw new ForbiddenException("Non puoi vedere gli acquirenti");
    }

    public UtenteResponse getAcquirente(Long id) {
        UtenteGenerico u = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        if (!(u instanceof Acquirente acquirente)) {
            throw new BadRequestException("L'utente selezionato non è un Acquirente");
        }

        return UtenteResponse.from(acquirente);
    }

    public void deleteAcquirente(Long id) {
        UtenteGenerico u = getUtenteLoggato();
        UtenteGenerico target = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        if (!(target instanceof Acquirente acquirente)) {
            throw new BadRequestException("L'utente selezionato non è un Acquirente");
        }

        if (!u.getId().equals(acquirente.getId()) && !(u instanceof GestorePiattaforma)) {
            throw new ForbiddenException("Non puoi eliminare questo acquirente");
        }

        utentiRepository.delete(acquirente);
    }

    public UtenteResponse patchAcquirente(Long id, UpdateAcquirenteRequest request) {
        UtenteGenerico u = getUtenteLoggato();
        UtenteGenerico target = utentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        if (!(target instanceof Acquirente acquirente)) {
            throw new BadRequestException("L'utente selezionato non è un Acquirente");
        }

        if (!u.getId().equals(acquirente.getId()) && !(u instanceof GestorePiattaforma)) {
            throw new ForbiddenException("Non puoi modificare questo acquirente");
        }

        if (request.email() != null) acquirente.setEmail(request.email());
        if (request.password() != null) acquirente.setPassword(passwordEncoder.encode(request.password()));

        return UtenteResponse.from(utentiRepository.save(acquirente));
    }

    // HELPER

    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return user;
    }

}