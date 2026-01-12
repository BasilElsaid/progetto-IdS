package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreatePacchettoRequest;
import it.unicam.filiera.prodotto.Pacchetto;
import it.unicam.filiera.repositories.PacchettoRepository;
import org.springframework.stereotype.Service;

@Service
public class PacchettiService {

    private final PacchettoRepository repository;

    public PacchettiService(PacchettoRepository repository) {
        this.repository = repository;
    }

    public Pacchetto crea(CreatePacchettoRequest request) {
        Pacchetto p = new Pacchetto();
        p.setNome(request.getNome());
        p.setPrezzo(request.getPrezzo());
        return repository.save(p);
    }
}
