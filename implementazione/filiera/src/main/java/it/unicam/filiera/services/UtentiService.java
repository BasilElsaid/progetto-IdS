package it.unicam.filiera.services;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;

@Service
public class UtentiService {

    private final AcquirenteRepository repository;

    public UtentiService(AcquirenteRepository repository) {
        this.repository = repository;
    }

    public Acquirente getCurrentUser() {
        List<Acquirente> utenti = repository.findAll();
        if (utenti.isEmpty()) {
            throw new RuntimeException("Nessun utente presente");
        }
        return utenti.get(0);
    }

    public Acquirente getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public Acquirente save(Acquirente u) {
        return repository.save(u);
    }
}
