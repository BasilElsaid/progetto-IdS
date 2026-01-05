package it.unicam.filiera.services;

import java.util.List;
import org.springframework.stereotype.Service;
import it.unicam.filiera.models.Prodotto;
import it.unicam.filiera.repositories.ProdottoRepository;

@Service
public class ProdottiService {

    private final ProdottoRepository repo;
    
    public List<Prodotto> list(String nome, String origine) {
        return repo.findAll();
    }
    


    public ProdottiService(ProdottoRepository repo) {
        this.repo = repo;
    }

    public Prodotto crea(Prodotto p) {
        return repo.save(p);
    }

    public Prodotto get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public List<Prodotto> all() {
        return repo.findAll();
    }
    public Prodotto update(Long id, Prodotto p) {
        return p;
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Prodotto addProcesso(Long id, String descrizione) {
        return get(id);
    }

    public Prodotto addCertificazione(Long id, String nome) {
        return get(id);
    }

}
