package it.unicam.filiera.controllers.dto.response;

import it.unicam.filiera.domain.AnnuncioProdotto;

import java.time.LocalDateTime;

public class AnnuncioProdottoResponse {
    public Long id;
    public Long aziendaId;
    public Long prodottoId;
    public double prezzo;
    public int stock;
    public boolean attivo;
    public LocalDateTime creatoIl;

    public static AnnuncioProdottoResponse from(AnnuncioProdotto a) {
        AnnuncioProdottoResponse r = new AnnuncioProdottoResponse();
        r.id = a.getId();
        r.aziendaId = a.getAzienda().getId();
        r.prodottoId = a.getProdotto().getId();
        r.prezzo = a.getPrezzo();
        r.stock = a.getStock();
        r.attivo = a.isAttivo();
        r.creatoIl = a.getCreatoIl();
        return r;
    }
}
