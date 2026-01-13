package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.AcquirenteRepository;
import org.springframework.stereotype.Service;

@Service
public class AcquirentiService {

    private final AcquirenteRepository repository;

    public AcquirentiService(AcquirenteRepository repository) {
        this.repository = repository;
    }

    public Acquirente crea(CreateAcquirenteRequest request) {
        Acquirente a = new Acquirente();
        a.setUsername(request.getUsername());
        a.setEmail(request.getEmail());
        a.setRuolo(Ruolo.ACQUIRENTE);
        return repository.save(a);
    }
}

