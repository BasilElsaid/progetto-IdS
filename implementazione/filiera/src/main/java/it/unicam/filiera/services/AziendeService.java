package it.unicam.filiera.services;

import java.util.List;

import org.springframework.stereotype.Service;

import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.TipoAzienda;
import it.unicam.filiera.repositories.AziendaRepository;

@Service
public class AziendeService {

    private final AziendaRepository repo;

    public AziendeService(AziendaRepository repo) {
        this.repo = repo;
    }

    public Azienda create(Azienda a) {
        return repo.save(a);
    }

    public Azienda get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Azienda update(Long id, Azienda updated) {
        Azienda a = get(id);
        a.setNome(updated.getNome());
        a.setSede(updated.getSede());
        a.setTipo(updated.getTipo());
        return repo.save(a);
    }

    public List<Azienda> byTipo(TipoAzienda tipo) {
        return repo.findByTipo(tipo);
    }
}
