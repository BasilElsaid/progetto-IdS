package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.AnnuncioMarketplace;

public class AnnuncioMarketplaceResponse {
    public Long id;
    public Long aziendaId;
    public Long prodottoId;
    public double prezzo;
    public int stock;
    public boolean attivo;

    public static AnnuncioMarketplaceResponse from(AnnuncioMarketplace a) {
        AnnuncioMarketplaceResponse r = new AnnuncioMarketplaceResponse();
        r.id = a.getId();
        r.aziendaId = a.getAzienda().getId();
        r.prodottoId = a.getProdotto().getId();
        r.prezzo = a.getPrezzo();
        r.stock = a.getStock();
        r.attivo = a.isAttivo();
        return r;
    }
}