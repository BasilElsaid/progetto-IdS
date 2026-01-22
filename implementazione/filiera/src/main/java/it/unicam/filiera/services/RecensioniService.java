package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.domain.Recensione;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import it.unicam.filiera.repositories.RecensioneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecensioniService {

    private final RecensioneRepository repo;
    private final OrdineRepository ordiniRepo;
    private final AcquirenteRepository acquirentiRepo;

    public RecensioniService(RecensioneRepository repo,
                             OrdineRepository ordiniRepo,
                             AcquirenteRepository acquirentiRepo) {
        this.repo = repo;
        this.ordiniRepo = ordiniRepo;
        this.acquirentiRepo = acquirentiRepo;
    }

    @Transactional
    public Recensione crea(Long acquirenteId, Long ordineId, int voto, String testo) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId)) {
            throw new BadRequestException("L'acquirente non possiede questo ordine");
        }

        Acquirente acquirente = ordine.getAcquirente();
        Recensione recensione = new Recensione(ordine, acquirente, voto, testo);

        return repo.save(recensione);
    }

    public List<Recensione> byOrdine(Long ordineId) {
        return repo.findByOrdineId(ordineId);
    }

    public List<Recensione> byAcquirente(Long acquirenteId) {
        return repo.findByAcquirenteId(acquirenteId);
    }
}