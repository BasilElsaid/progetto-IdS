package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;

import java.time.LocalDateTime;

public record SpedizioneResponse(
        Long ordineId,
        StatoOrdine stato,
        String trackingCode,
        LocalDateTime dataSpedizione,
        LocalDateTime dataConsegna,
        String messaggio
) {
    public static SpedizioneResponse from(Ordine ordine, String messaggio) {
        return new SpedizioneResponse(
                ordine.getId(),
                ordine.getStato(),
                ordine.getTrackingCode(),
                ordine.getDataSpedizione(),
                ordine.getDataConsegna(),
                messaggio
        );
    }
}