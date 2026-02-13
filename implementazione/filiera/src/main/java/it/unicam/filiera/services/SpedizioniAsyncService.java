package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.repositories.AcquirentiRepository;
import it.unicam.filiera.repositories.AnnunciPacchettiRepository;
import it.unicam.filiera.repositories.AnnunciProdottiRepository;
import it.unicam.filiera.repositories.OrdiniRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SpedizioniAsyncService {

    private final OrdiniRepository ordineRepo;
    private final AnnunciProdottiRepository prodottiRepo;
    private final AnnunciPacchettiRepository pacchettiRepo;
    private final AcquirentiRepository acquirenteRepo;

    public SpedizioniAsyncService(OrdiniRepository ordineRepo,
                                  AnnunciProdottiRepository prodottiRepo,
                                  AnnunciPacchettiRepository pacchettiRepo,
                                  AcquirentiRepository acquirenteRepo) {
        this.ordineRepo = ordineRepo;
        this.prodottiRepo = prodottiRepo;
        this.pacchettiRepo = pacchettiRepo;
        this.acquirenteRepo = acquirenteRepo;
    }

    @Async
    public void gestisciOrdineAutomatico(Long ordineId) {
        try {
            // 1 minuto -> SPEDITO
            Thread.sleep(60_000);
            setOrdineSpedito(ordineId);

            // 1 altro minuto -> CONSEGNATO
            Thread.sleep(60_000);
            setOrdineConsegnato(ordineId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Transactional
    protected void setOrdineSpedito(Long ordineId) {
        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new IllegalStateException("Ordine non trovato"));
        if (ordine.getStato() == StatoOrdine.PAGATO) {
            ordine.setStato(StatoOrdine.SPEDITO);
            ordine.setDataSpedizione(LocalDateTime.now());
            ordine.setTrackingCode("AUTO-" + ordine.getId());
            ordineRepo.saveAndFlush(ordine); // commit immediato
        }
    }

    @Transactional
    protected void setOrdineConsegnato(Long ordineId) {
        Ordine ordine = ordineRepo.findById(ordineId)
                .orElseThrow(() -> new IllegalStateException("Ordine non trovato"));
        if (ordine.getStato() == StatoOrdine.SPEDITO) {
            ordine.setStato(StatoOrdine.CONSEGNATO);
            ordine.setDataConsegna(LocalDateTime.now());
            ordineRepo.save(ordine);
        }
    }
}
