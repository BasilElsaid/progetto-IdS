package it.unicam.filiera.controllers.dto.response;

import it.unicam.filiera.domain.Recensione;

public class RecensioneResponse {
    public Long id;
    public Long ordineId;
    public Long acquirenteId;
    public int voto;
    public String testo;

    public static RecensioneResponse from(Recensione r) {
        RecensioneResponse resp = new RecensioneResponse();
        resp.id = r.getId();
        resp.ordineId = r.getOrdine().getId();
        resp.acquirenteId = r.getAcquirente().getId();
        resp.voto = r.getVoto();
        resp.testo = r.getTesto();
        return resp;
    }
}