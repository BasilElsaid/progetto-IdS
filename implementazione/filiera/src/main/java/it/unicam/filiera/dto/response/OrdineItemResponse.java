package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.OrdineItem;

public class OrdineItemResponse {

    private Long id;
    private Long annuncioId;
    private int quantita;
    private double prezzoUnitario;
    private boolean pacchetto;

    public static OrdineItemResponse from(OrdineItem item) {
        OrdineItemResponse r = new OrdineItemResponse();
        r.id = item.getId();
        r.annuncioId = item.getAnnuncioId();
        r.quantita = item.getQuantita();
        r.prezzoUnitario = item.getPrezzoUnitario();
        r.pacchetto = item.isPacchetto();
        return r;
    }

    // getters e setters
    public Long getId() { return id; }
    public Long getAnnuncioId() { return annuncioId; }
    public int getQuantita() { return quantita; }
    public double getPrezzoUnitario() { return prezzoUnitario; }
    public boolean isPacchetto() { return pacchetto; }

    public void setId(Long id) { this.id = id; }
    public void setAnnuncioId(Long annuncioId) { this.annuncioId = annuncioId; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
    public void setPrezzoUnitario(double prezzoUnitario) { this.prezzoUnitario = prezzoUnitario; }
    public void setPacchetto(boolean pacchetto) { this.pacchetto = pacchetto; }
}