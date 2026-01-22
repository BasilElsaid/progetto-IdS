package it.unicam.filiera.controllers.dto.response;

import it.unicam.filiera.domain.AnnuncioPacchetto;

import java.time.LocalDateTime;

public class AnnuncioPacchettoResponse {

    public Long id;
    public Long aziendaId;
    public Long pacchettoId;
    public double prezzo;
    public int stock;
    public boolean attivo;
    public LocalDateTime creatoIl;

    public static AnnuncioPacchettoResponse from(AnnuncioPacchetto a) {
        AnnuncioPacchettoResponse r = new AnnuncioPacchettoResponse();
        r.id = a.getId();
        r.aziendaId = a.getAzienda().getId();
        r.pacchettoId = a.getPacchetto().getId();
        r.prezzo = a.getPrezzo();
        r.stock = a.getStock();
        r.attivo = a.isAttivo();
        r.creatoIl = a.getCreatoIl();
        return r;
    }
}
