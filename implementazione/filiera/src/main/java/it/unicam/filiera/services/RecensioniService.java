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
    private final ProdottoRepository prodottiRepo;
    private final AcquirenteRepository acquirentiRepo;

    public RecensioniService(RecensioneRepository repo,
                             OrdineRepository ordiniRepo,
                             ProdottoRepository prodottiRepo,
                             AcquirenteRepository acquirentiRepo) {
        this.repo = repo;
        this.ordiniRepo = ordiniRepo;
        this.prodottiRepo = prodottiRepo;
        this.acquirentiRepo = acquirentiRepo;
    }

    @Transactional
    public Recensione crea(Long acquirenteId, Long ordineId, Long prodottoId, int voto, String testo) {
        if (voto < 1 || voto > 5) throw new BadRequestException("Voto deve essere 1..5");

        Acquirente a = acquirentiRepo.findById(acquirenteId)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId)) {
            throw new BadRequestException("Ordine non appartiene a questo acquirente");
        }

        if (ordine.getStato() != StatoOrdine.CONSEGNATO && ordine.getStato() != StatoOrdine.RIMBORSO_RICHIESTO
                && ordine.getStato() != StatoOrdine.RIMBORSATO && ordine.getStato() != StatoOrdine.RIMBORSO_RIFIUTATO) {
            throw new BadRequestException("Puoi recensire solo dopo CONSEGNA");
        }

        boolean prodottoInOrdine = ordine.getItemsMarketplace().stream()
                .anyMatch(i -> i.getProdotto().getId().equals(prodottoId));

        if (!prodottoInOrdine) {
            throw new BadRequestException("Questo prodotto non è presente nell'ordine");
        }

        if (repo.existsByOrdineIdAndProdottoIdAndAcquirenteId(ordineId, prodottoId, acquirenteId)) {
            throw new BadRequestException("Recensione già inserita per questo prodotto in questo ordine");
        }

        Prodotto p = prodottiRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

        Recensione r = new Recensione(ordine, p, a, voto, testo);
        return repo.save(r);
    }

    public List<Recensione> byProdotto(Long prodottoId) {
        return repo.findByProdottoId(prodottoId);
    }

    public List<Recensione> byAcquirente(Long acquirenteId) {
        return repo.findByAcquirenteId(acquirenteId);
    }
}
