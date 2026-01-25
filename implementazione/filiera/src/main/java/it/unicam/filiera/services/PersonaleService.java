package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.dto.update.UpdatePersonaleRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.models.Curatore;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.repositories.AnimatoreRepository;
import it.unicam.filiera.repositories.CuratoreRepository;
import it.unicam.filiera.repositories.GestorePiattaformaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaleService {

    private final CuratoreRepository curatoreRepository;
    private final AnimatoreRepository animatoreRepository;
    private final GestorePiattaformaRepository gestoreRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonaleService(
            CuratoreRepository curatoreRepository,
            AnimatoreRepository animatoreRepository,
            GestorePiattaformaRepository gestoreRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.curatoreRepository = curatoreRepository;
        this.animatoreRepository = animatoreRepository;
        this.gestoreRepository = gestoreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UtenteResponse creaPersonale(CreatePersonaleRequest request) {
        switch (request.ruolo()) {
            case CURATORE:
                if (curatoreRepository.count() > 0) {
                    throw new BadRequestException("Curatore già presente");
                }
                Curatore c = new Curatore();
                c.setUsername(request.username());
                c.setEmail(request.email());
                c.setPassword(passwordEncoder.encode(request.password()));
                c.setNome(request.nome());
                c.setCognome(request.cognome());
                c.setTelefono(request.telefono());
                c.setRuolo(Ruolo.CURATORE);
                return UtenteResponse.from(curatoreRepository.save(c));

            case ANIMATORE:
                if (animatoreRepository.count() > 0) {
                    throw new BadRequestException("Animatore già presente");
                }
                Animatore a = new Animatore();
                a.setUsername(request.username());
                a.setEmail(request.email());
                a.setPassword(passwordEncoder.encode(request.password()));
                a.setNome(request.nome());
                a.setCognome(request.cognome());
                a.setTelefono(request.telefono());
                a.setRuolo(Ruolo.ANIMATORE);
                return UtenteResponse.from(animatoreRepository.save(a));

            case GESTORE_PIATTAFORMA:
                if (gestoreRepository.count() > 0) {
                    throw new BadRequestException("Gestore piattaforma già presente");
                }
                GestorePiattaforma g = new GestorePiattaforma();
                g.setUsername(request.username());
                g.setEmail(request.email());
                g.setPassword(passwordEncoder.encode(request.password()));
                g.setNome(request.nome());
                g.setCognome(request.cognome());
                g.setTelefono(request.telefono());
                g.setRuolo(Ruolo.GESTORE_PIATTAFORMA);
                return UtenteResponse.from(gestoreRepository.save(g));

            default:
                throw new BadRequestException("Ruolo non valido per il personale");
        }
    }

    public List<UtenteResponse> listaPersonale() {
        List<UtenteResponse> out = new ArrayList<>();
        curatoreRepository.findAll().stream().findFirst().ifPresent(u -> out.add(UtenteResponse.from(u)));
        animatoreRepository.findAll().stream().findFirst().ifPresent(u -> out.add(UtenteResponse.from(u)));
        gestoreRepository.findAll().stream().findFirst().ifPresent(u -> out.add(UtenteResponse.from(u)));
        return out;
    }

    public UtenteResponse getPersonale(Long id) {
        return curatoreRepository.findById(id)
                .map(UtenteResponse::from)
                .or(() -> animatoreRepository.findById(id).map(UtenteResponse::from))
                .or(() -> gestoreRepository.findById(id).map(UtenteResponse::from))
                .orElseThrow(() -> new NotFoundException("Personale non trovato"));
    }

    public void eliminaPersonale(Long id) {
        if(curatoreRepository.existsById(id)) {
            curatoreRepository.deleteById(id);
            return;
        }
        if(animatoreRepository.existsById(id)) {
            animatoreRepository.deleteById(id);
            return;
        }
        if(gestoreRepository.existsById(id)) {
            gestoreRepository.deleteById(id);
            return;
        }
        throw new NotFoundException("Personale non trovato");
    }

    public UtenteResponse patchPersonale(Long id, UpdatePersonaleRequest request) {
        if (curatoreRepository.existsById(id)) {
            Curatore c = curatoreRepository.findById(id).get();
            updateUtente(c, request);
            return UtenteResponse.from(curatoreRepository.save(c));
        }
        if (animatoreRepository.existsById(id)) {
            Animatore a = animatoreRepository.findById(id).get();
            updateUtente(a, request);
            return UtenteResponse.from(animatoreRepository.save(a));
        }
        if (gestoreRepository.existsById(id)) {
            GestorePiattaforma g = gestoreRepository.findById(id).get();
            updateUtente(g, request);
            return UtenteResponse.from(gestoreRepository.save(g));
        }
        throw new NotFoundException("Personale non trovato");
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