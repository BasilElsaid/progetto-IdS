package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.domain.RichiestaRimborso;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.enums.StatoPagamento;
import it.unicam.filiera.enums.StatoRimborso;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import it.unicam.filiera.repositories.PagamentoOrdineRepository;
import it.unicam.filiera.repositories.RichiestaRimborsoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RimborsiService {

    private static final int GIORNI_FINESTRA_RIMBORSO = 14;

    private final RichiestaRimborsoRepository repo;
    private final OrdineRepository ordiniRepo;
    private final AcquirenteRepository acquirentiRepo;
    private final PagamentoOrdineRepository pagamentiRepo;

    public RimborsiService(RichiestaRimborsoRepository repo,
                           OrdineRepository ordiniRepo,
                           AcquirenteRepository acquirentiRepo,
                           PagamentoOrdineRepository pagamentiRepo) {
        this.repo = repo;
        this.ordiniRepo = ordiniRepo;
        this.acquirentiRepo = acquirentiRepo;
        this.pagamentiRepo = pagamentiRepo;
    }

    @Transactional
    public RichiestaRimborso richiedi(Long acquirenteId, Long ordineId, String motivazione) {
        if (motivazione == null || motivazione.isBlank()) {
            throw new BadRequestException("Motivazione obbligatoria");
        }

        if (repo.existsByOrdineId(ordineId)) {
            throw new BadRequestException("Rimborso già richiesto per questo ordine");
        }

        Acquirente acquirente = acquirentiRepo.findById(acquirenteId)
                .orElseThrow(() -> new NotFoundException("Acquirente non trovato"));

        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (!ordine.getAcquirente().getId().equals(acquirenteId)) {
            throw new BadRequestException("Ordine non appartiene a questo acquirente");
        }

        if (ordine.getStato() != StatoOrdine.CONSEGNATO) {
            throw new BadRequestException("Puoi chiedere rimborso solo dopo CONSEGNA");
        }

        if (ordine.getDataConsegna() == null) {
            throw new BadRequestException("Data consegna mancante");
        }

        LocalDateTime deadline = ordine.getDataConsegna().plusDays(GIORNI_FINESTRA_RIMBORSO);
        if (LocalDateTime.now().isAfter(deadline)) {
            throw new BadRequestException("Finestra rimborso scaduta (14 giorni)");
        }

        ordine.setStato(StatoOrdine.RIMBORSO_RICHIESTO);
        ordiniRepo.save(ordine);

        RichiestaRimborso r = new RichiestaRimborso(ordine, acquirente, motivazione.trim());
        return repo.save(r);
    }

    public List<RichiestaRimborso> mie(Long acquirenteId) {
        return repo.findByAcquirenteId(acquirenteId);
    }

    @Transactional
    public RichiestaRimborso valuta(Long rimborsoId, boolean approva, String notaGestore) {
        RichiestaRimborso r = repo.findById(rimborsoId)
                .orElseThrow(() -> new NotFoundException("Richiesta rimborso non trovata"));

        if (r.getStato() != StatoRimborso.IN_VALUTAZIONE) {
            throw new BadRequestException("Rimborso già valutato");
        }

        Ordine ordine = r.getOrdine();

        r.setNotaGestore(notaGestore);
        r.setDecisoIl(LocalDateTime.now());

        if (approva) {
            r.setStato(StatoRimborso.APPROVATO);
            ordine.setStato(StatoOrdine.RIMBORSATO);

            PagamentoOrdine p = PagamentoOrdine.creaRimborso(ordine, MetodoPagamento.PAYPAL, ordine.getImportoTotale());
            p.setStato(StatoPagamento.RIMBORSATO);
            p.setPagatoIl(LocalDateTime.now());
            pagamentiRepo.save(p);

        } else {
            r.setStato(StatoRimborso.RIFIUTATO);
            ordine.setStato(StatoOrdine.RIMBORSO_RIFIUTATO);
        }

        ordiniRepo.save(ordine);
        return repo.save(r);
    }
}
