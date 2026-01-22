package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.RichiestaRimborso;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.StatoRimborso;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.RimborsiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RimborsiService {

    private final RimborsiRepository repo;
    private final OrdineRepository ordiniRepo;
    private final AcquirenteRepository acquirentiRepo;

    public RimborsiService(RimborsiRepository repo,
                           OrdineRepository ordiniRepo,
                           AcquirenteRepository acquirentiRepo) {
        this.repo = repo;
        this.ordiniRepo = ordiniRepo;
        this.acquirentiRepo = acquirentiRepo;
    }

    @Transactional
    public RichiestaRimborso richiedi(Long acquirenteId, Long ordineId, String motivazione) {
        Acquirente a = acquirentiRepo.findById(acquirenteId)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId)) {
            throw new BadRequestException("Ordine non appartiene a questo acquirente");
        }

        if (ordine.getStato() != StatoOrdine.PAGATO && ordine.getStato() != StatoOrdine.CONSEGNATO) {
            throw new BadRequestException("Rimborso possibile solo per ordini PAGATI o CONSEGNATI");
        }

        RichiestaRimborso r = new RichiestaRimborso(ordine, a, motivazione);

        // Aggiorna lo stato dell'ordine in richiesta rimborso
        ordine.setStato(StatoOrdine.RIMBORSO_RICHIESTO);
        ordiniRepo.save(ordine);

        return repo.save(r);
    }

    public List<RichiestaRimborso> mie(Long acquirenteId) {
        return repo.findByAcquirenteId(acquirenteId);
    }

    @Transactional
    public RichiestaRimborso valuta(Long rimborsoId, boolean approva, String notaGestore) {
        RichiestaRimborso r = repo.findById(rimborsoId)
                .orElseThrow(() -> new NotFoundException("Rimborso non trovato"));

        r.setNotaGestore(notaGestore);
        r.setStato(approva ? StatoRimborso.APPROVATO : StatoRimborso.RIFIUTATO);
        r.setDecisoIl(LocalDateTime.now());

        // Aggiorna lo stato ordine in base alla decisione
        Ordine ordine = r.getOrdine();
        if (approva) {
            ordine.setStato(StatoOrdine.RIMBORSATO);
        } else {
            ordine.setStato(StatoOrdine.RIMBORSO_RIFIUTATO);
        }
        ordiniRepo.save(ordine);

        return repo.save(r);
    }
}