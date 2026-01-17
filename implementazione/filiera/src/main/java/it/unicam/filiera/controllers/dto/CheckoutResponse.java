package it.unicam.filiera.controllers.dto;

public class CheckoutResponse {
    private Long ordineId;
    private String stato;
    private double totale;
    private Long annuncioId;
    private int quantita;

    public Long getOrdineId() { return ordineId; }
    public void setOrdineId(Long ordineId) { this.ordineId = ordineId; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public double getTotale() { return totale; }
    public void setTotale(double totale) { this.totale = totale; }

    public Long getAnnuncioId() { return annuncioId; }
    public void setAnnuncioId(Long annuncioId) { this.annuncioId = annuncioId; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
}