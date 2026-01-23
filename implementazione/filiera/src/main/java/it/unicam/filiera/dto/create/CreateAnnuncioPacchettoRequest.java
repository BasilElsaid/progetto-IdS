package it.unicam.filiera.dto.create;

public class CreateAnnuncioPacchettoRequest {

    private Long aziendaId;
    private Long pacchettoId;
    private double prezzo;
    private int stock;
    private boolean attivo = true;

    public CreateAnnuncioPacchettoRequest() {}

    public Long getAziendaId() { return aziendaId; }
    public void setAziendaId(Long aziendaId) { this.aziendaId = aziendaId; }

    public Long getPacchettoId() { return pacchettoId; }
    public void setPacchettoId(Long pacchettoId) { this.pacchettoId = pacchettoId; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isAttivo() { return attivo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }
}
