package it.unicam.filiera.controllers.dto.response;

import it.unicam.filiera.domain.Recensione;

public class RecensioneResponse {
    public Long id;
    public Long ordineId;
    public Long itemId;       // prodotto o pacchetto
    public boolean pacchetto; // true se recensione su pacchetto
    public Long acquirenteId;
    public int voto;
    public String testo;

    public static RecensioneResponse from(Recensione r) {
        RecensioneResponse resp = new RecensioneResponse();
        resp.id = r.getId();
        resp.ordineId = r.getOrdine().getId();
        // Se è recensione prodotto, prendo l'id del prodotto; se pacchetto, prendo lo stesso id del pacchetto (o dell'item)
        resp.itemId = r.getProdotto() != null ? r.getProdotto().getId() : null;
        resp.pacchetto = r.isPacchetto();
        resp.acquirenteId = r.getAcquirente().getId();
        resp.voto = r.getVoto();
        resp.testo = r.getTesto();
        return resp;
    }
}