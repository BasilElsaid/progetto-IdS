package it.unicam.filiera.services;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;
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
    public String spedisci(Long ordineId, String trackingCode) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (ordine.getStato() != StatoOrdine.PAGATO) {
            return "Ordine " + ordineId + " non può essere spedito: stato non PAGATO";
        }

        ordine.setStato(StatoOrdine.SPEDITO);
        ordine.setDataSpedizione(LocalDateTime.now());
        if (trackingCode != null && !trackingCode.isBlank()) {
            ordine.setTrackingCode(trackingCode.trim());
        }

        ordiniRepo.save(ordine);
        return "Ordine " + ordineId + " spedito con tracking: " + trackingCode;
    }

    @Transactional
    public String consegna(Long ordineId) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (ordine.getStato() != StatoOrdine.SPEDITO) {
            return "Ordine " + ordineId + " non può essere consegnato: stato non SPEDITO";
        }

        ordine.setStato(StatoOrdine.CONSEGNATO);
        ordine.setDataConsegna(LocalDateTime.now());
        ordiniRepo.save(ordine);
        return "Ordine " + ordineId + " consegnato";
    }
}