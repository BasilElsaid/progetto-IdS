package it.unicam.filiera.controllers.dto.create;

public class CreateAnnuncioProdottoRequest {

    private Long aziendaId;
    private Long prodottoId;
    private double prezzo;
    private int stock;
    private boolean attivo = true;

    public CreateAnnuncioProdottoRequest() {}

    public Long getAziendaId() { return aziendaId; }
    public void setAziendaId(Long aziendaId) { this.aziendaId = aziendaId; }

    public Long getProdottoId() { return prodottoId; }
    public void setProdottoId(Long prodottoId) { this.prodottoId = prodottoId; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isAttivo() { return attivo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }
}
