package it.unicam.filiera.services;

import it.unicam.filiera.domain.AnnuncioPacchetto;
import it.unicam.filiera.domain.AnnuncioProdotto;
import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.OrdineItem;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.repositories.AcquirenteRepository;
import it.unicam.filiera.repositories.AnnuncioPacchettoRepository;
import it.unicam.filiera.repositories.AnnuncioProdottoRepository;
import it.unicam.filiera.repositories.OrdineRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SpedizioniAsyncService {

    private final OrdineRepository ordineRepo;
    private final AnnuncioProdottoRepository prodottiRepo;
    private final AnnuncioPacchettoRepository pacchettiRepo;
    private final AcquirenteRepository acquirenteRepo;

    public SpedizioniAsyncService(OrdineRepository ordineRepo,
                                  AnnuncioProdottoRepository prodottiRepo,
                                  AnnuncioPacchettoRepository pacchettiRepo,
                                  AcquirenteRepository acquirenteRepo) {
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

            gestisciPostConsegna(ordine);
        }
    }

    @Transactional
    protected void gestisciPostConsegna(Ordine ordine) {
        Acquirente acquirente = ordine.getAcquirente();

        for (OrdineItem item : ordine.getItems()) {
            if (item.isPacchetto()) {
                AnnuncioPacchetto annuncio = pacchettiRepo.findById(item.getAnnuncioId()).orElse(null);
                if (annuncio != null) {
                    acquirente.getPacchettiAcquistati().add(annuncio.getPacchetto());
                    if (annuncio.getStock() <= 0) {
                        annuncio.setAttivo(false);
                        pacchettiRepo.save(annuncio);
                    }
                }
            } else {
                AnnuncioProdotto annuncio = prodottiRepo.findById(item.getAnnuncioId()).orElse(null);
                if (annuncio != null) {
                    acquirente.getProdottiAcquistati().add(annuncio.getProdotto());
                    if (annuncio.getStock() <= 0) {
                        annuncio.setAttivo(false);
                        prodottiRepo.save(annuncio);
                    }
                }
            }
        }

        acquirenteRepo.save(acquirente);
    }
}
