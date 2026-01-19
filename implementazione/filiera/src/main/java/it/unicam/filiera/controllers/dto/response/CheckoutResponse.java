package it.unicam.filiera.controllers.dto.response;

import it.unicam.filiera.domain.Ordine;

public class CheckoutResponse {

    private Long ordineId;
    private double importoTotale;
    private String stato;

    public CheckoutResponse() {}

    public static CheckoutResponse from(Ordine o) {
        CheckoutResponse r = new CheckoutResponse();
        r.ordineId = o.getId();
        r.importoTotale = o.getImportoTotale();
        r.stato = o.getStato().name();
        return r;
    }

    public Long getOrdineId() { return ordineId; }
    public double getImportoTotale() { return importoTotale; }
    public String getStato() { return stato; }
}
