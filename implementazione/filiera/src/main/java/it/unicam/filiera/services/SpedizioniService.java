package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.repositories.OrdineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SpedizioniService {

    private final OrdineRepository ordiniRepo;

    public SpedizioniService(OrdineRepository ordiniRepo) {
        this.ordiniRepo = ordiniRepo;
    }

    @Transactional
    public Ordine spedisci(Long ordineId, String trackingCode) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (ordine.getStato() != StatoOrdine.PAGATO) {
            throw new BadRequestException("Puoi spedire solo ordini PAGATI");
        }

        LocalDateTime now = LocalDateTime.now();
        ordine.setStato(StatoOrdine.SPEDITO);
        ordine.setDataSpedizione(now);
        if (trackingCode != null && !trackingCode.isBlank()) {
            ordine.setTrackingCode(trackingCode.trim());
        }

        return ordiniRepo.save(ordine);
    }

    @Transactional
    public Ordine consegna(Long ordineId) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (ordine.getStato() != StatoOrdine.SPEDITO) {
            throw new BadRequestException("Puoi consegnare solo ordini SPEDITI");
        }

        ordine.setStato(StatoOrdine.CONSEGNATO);
        ordine.setDataConsegna(LocalDateTime.now());
        return ordiniRepo.save(ordine);
    }
}
