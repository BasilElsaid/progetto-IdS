package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreatePersonaleRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.models.Curatore;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.models.Ruolo;
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
                    throw new RuntimeException("Curatore già presente");
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
                    throw new RuntimeException("Animatore già presente");
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
                    throw new RuntimeException("Gestore piattaforma già presente");
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
                throw new RuntimeException("Ruolo non valido per il personale");
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
                .orElseThrow(() -> new RuntimeException("Personale non trovato"));
    }
}