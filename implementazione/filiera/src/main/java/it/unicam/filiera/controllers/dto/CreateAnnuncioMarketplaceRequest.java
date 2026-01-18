package it.unicam.filiera.controllers.dto;

public class CreateAnnuncioMarketplaceRequest {
    private Long aziendaId;
    private Long prodottoId;
    private double prezzo;
    private int stock;

    public Long getAziendaId() { return aziendaId; }
    public void setAziendaId(Long aziendaId) { this.aziendaId = aziendaId; }

    public Long getProdottoId() { return prodottoId; }
    public void setProdottoId(Long prodottoId) { this.prodottoId = prodottoId; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
