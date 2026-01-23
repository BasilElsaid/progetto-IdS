package it.unicam.filiera.services;

import it.unicam.filiera.dto.response.SpedizioneResponse;
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
    public SpedizioneResponse spedisci(Long ordineId, String trackingCode) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (ordine.getStato() == StatoOrdine.CONSEGNATO) {
            throw new BadRequestException("Ordine " + ordineId + " è già CONSEGNATO");
        }

        if (ordine.getStato() != StatoOrdine.PAGATO) {
            throw new BadRequestException("Ordine " + ordineId + " non può essere spedito: stato non PAGATO");
        }

        ordine.setStato(StatoOrdine.SPEDITO);
        ordine.setDataSpedizione(LocalDateTime.now());

        if (trackingCode != null && !trackingCode.isBlank()) {
            ordine.setTrackingCode(trackingCode.trim());
        }

        Ordine saved = ordiniRepo.save(ordine);

        return SpedizioneResponse.from(saved,
                "Ordine " + ordineId + " spedito con tracking: " + saved.getTrackingCode());
    }

    @Transactional
    public SpedizioneResponse consegna(Long ordineId) {
        Ordine ordine = ordiniRepo.findById(ordineId)
                .orElseThrow(() -> new NotFoundException("Ordine non trovato"));

        if (ordine.getStato() != StatoOrdine.SPEDITO) {
            throw new BadRequestException("Ordine " + ordineId + " non può essere consegnato: stato non SPEDITO");
        }

        ordine.setStato(StatoOrdine.CONSEGNATO);
        ordine.setDataConsegna(LocalDateTime.now());

        Ordine saved = ordiniRepo.save(ordine);

        return SpedizioneResponse.from(saved,
                "Ordine " + ordineId + " consegnato");
    }
}