package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;

import java.time.LocalDateTime;

public class SpedizioneResponse {

    private Long ordineId;
    private StatoOrdine stato;
    private String trackingCode;
    private LocalDateTime dataSpedizione;
    private LocalDateTime dataConsegna;
    private String messaggio;

    public static SpedizioneResponse from(Ordine ordine, String messaggio) {
        SpedizioneResponse resp = new SpedizioneResponse();
        resp.ordineId = ordine.getId();
        resp.stato = ordine.getStato();
        resp.trackingCode = ordine.getTrackingCode();
        resp.dataSpedizione = ordine.getDataSpedizione();
        resp.dataConsegna = ordine.getDataConsegna();
        resp.messaggio = messaggio;
        return resp;
    }

    // --- GETTER ---

    public Long getOrdineId() { return ordineId; }
    public StatoOrdine getStato() { return stato; }
    public String getTrackingCode() { return trackingCode; }
    public LocalDateTime getDataSpedizione() { return dataSpedizione; }
    public LocalDateTime getDataConsegna() { return dataConsegna; }
    public String getMessaggio() { return messaggio; }
}