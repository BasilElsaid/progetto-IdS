package it.unicam.filiera.dto.update;

public class UpdateAnnuncioMarketplaceRequest {
    private Double prezzo;
    private Integer stock;
    private Boolean attivo;

    public Double getPrezzo() { return prezzo; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Boolean getAttivo() { return attivo; }
    public void setAttivo(Boolean attivo) { this.attivo = attivo; }
}
