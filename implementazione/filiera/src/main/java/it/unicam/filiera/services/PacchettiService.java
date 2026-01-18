package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CreatePacchettoRequest;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.repositories.PacchettoRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PacchettiService {

    private final PacchettoRepository pacchettoRepo;
    private final ProdottoRepository prodottoRepo;

    public PacchettiService(PacchettoRepository pacchettoRepo, ProdottoRepository prodottoRepo) {
        this.pacchettoRepo = pacchettoRepo;
        this.prodottoRepo = prodottoRepo;
    }

    public List<Pacchetto> lista() {
        return pacchettoRepo.findAll();
    }

    public Pacchetto get(Long id) {
        return pacchettoRepo.findById(id).orElseThrow();
    }

    public Pacchetto crea(CreatePacchettoRequest req) {
        Pacchetto p = new Pacchetto();
        p.setNome(req.getNome());
        p.setPrezzo(req.getPrezzo());

        List<Prodotto> prodotti = new ArrayList<>();
        if (req.getProdottiIds() != null) {
            for (Long pid : req.getProdottiIds()) {
                prodotti.add(prodottoRepo.findById(pid).orElseThrow());
            }
        }
        p.setProdotti(prodotti);

        return pacchettoRepo.save(p);
    }

    public void elimina(Long id) {
        pacchettoRepo.deleteById(id);
    }
}
