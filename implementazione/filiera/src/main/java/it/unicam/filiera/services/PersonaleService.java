package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreatePersonaleRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.models.Curatore;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.repositories.AnimatoreRepository;
import it.unicam.filiera.repositories.CuratoreRepository;
import it.unicam.filiera.repositories.GestorePiattaformaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonaleService {

    private final CuratoreRepository curatoreRepository;
    private final AnimatoreRepository animatoreRepository;
    private final GestorePiattaformaRepository gestoreRepository;

    public PersonaleService(
            CuratoreRepository curatoreRepository,
            AnimatoreRepository animatoreRepository,
            GestorePiattaformaRepository gestoreRepository
    ) {
        this.curatoreRepository = curatoreRepository;
        this.animatoreRepository = animatoreRepository;
        this.gestoreRepository = gestoreRepository;
    }

    public UtenteResponse creaPersonale(CreatePersonaleRequest request) {
        switch (request.getRuolo()) {
            case CURATORE:
                if (curatoreRepository.count() > 0) {
                    throw new BadRequestException("Curatore già presente");
                }
                Curatore c = new Curatore();
                c.setUsername(request.getUsername());
                c.setEmail(request.getEmail());
                c.setPassword(request.getPassword());
                c.setNome(request.getNome());
                c.setCognome(request.getCognome());
                c.setTelefono(request.getTelefono());
                c.setRuolo(Ruolo.CURATORE);
                return UtenteResponse.from(curatoreRepository.save(c));

            case ANIMATORE:
                if (animatoreRepository.count() > 0) {
                    throw new BadRequestException("Animatore già presente");
                }
                Animatore a = new Animatore();
                a.setUsername(request.getUsername());
                a.setEmail(request.getEmail());
                a.setPassword(request.getPassword());
                a.setNome(request.getNome());
                a.setCognome(request.getCognome());
                a.setTelefono(request.getTelefono());
                a.setRuolo(Ruolo.ANIMATORE);
                return UtenteResponse.from(animatoreRepository.save(a));

            case GESTORE_PIATTAFORMA:
                if (gestoreRepository.count() > 0) {
                    throw new BadRequestException("Gestore piattaforma già presente");
                }
                GestorePiattaforma g = new GestorePiattaforma();
                g.setUsername(request.getUsername());
                g.setEmail(request.getEmail());
                g.setPassword(request.getPassword());
                g.setNome(request.getNome());
                g.setCognome(request.getCognome());
                g.setTelefono(request.getTelefono());
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

    public UtenteResponse patchPersonale(Long id, CreatePersonaleRequest request) {
        if (curatoreRepository.existsById(id)) {
            Curatore c = curatoreRepository.findById(id).get();
            if(request.getEmail() != null) c.setEmail(request.getEmail());
            if(request.getPassword() != null) c.setPassword(request.getPassword());
            if(request.getNome() != null) c.setNome(request.getNome());
            if(request.getCognome() != null) c.setCognome(request.getCognome());
            if(request.getTelefono() != null) c.setTelefono(request.getTelefono());
            return UtenteResponse.from(curatoreRepository.save(c));
        }
        if (animatoreRepository.existsById(id)) {
            Animatore a = animatoreRepository.findById(id).get();
            if(request.getEmail() != null) a.setEmail(request.getEmail());
            if(request.getPassword() != null) a.setPassword(request.getPassword());
            if(request.getNome() != null) a.setNome(request.getNome());
            if(request.getCognome() != null) a.setCognome(request.getCognome());
            if(request.getTelefono() != null) a.setTelefono(request.getTelefono());
            return UtenteResponse.from(animatoreRepository.save(a));
        }
        if (gestoreRepository.existsById(id)) {
            GestorePiattaforma g = gestoreRepository.findById(id).get();
            if(request.getEmail() != null) g.setEmail(request.getEmail());
            if(request.getPassword() != null) g.setPassword(request.getPassword());
            if(request.getNome() != null) g.setNome(request.getNome());
            if(request.getCognome() != null) g.setCognome(request.getCognome());
            if(request.getTelefono() != null) g.setTelefono(request.getTelefono());
            return UtenteResponse.from(gestoreRepository.save(g));
        }
        throw new NotFoundException("Personale non trovato");
    }
}