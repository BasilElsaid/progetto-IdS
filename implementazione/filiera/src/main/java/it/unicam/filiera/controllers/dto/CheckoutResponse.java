package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.Ordine;

import java.time.LocalDateTime;

public class CheckoutResponse {
    public Long ordineId;
    public double importoTotale;
    public String stato;
    public LocalDateTime dataOra;

    public static CheckoutResponse from(Ordine o) {
        CheckoutResponse r = new CheckoutResponse();
        r.ordineId = o.getId();
        r.importoTotale = o.getTotale();
        r.stato = (o.getStato() == null) ? null : o.getStato().name();
        r.dataOra = o.getDataOra();
        return r;
    }
}
