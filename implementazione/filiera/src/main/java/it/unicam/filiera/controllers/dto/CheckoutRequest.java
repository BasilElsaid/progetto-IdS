package it.unicam.filiera.controllers.dto;

public class CheckoutRequest {
    private Long annuncioId;
    private int quantita;
    private Long acquirenteId;

    public Long getAnnuncioId() { return annuncioId; }
    public void setAnnuncioId(Long annuncioId) { this.annuncioId = annuncioId; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public Long getAcquirenteId() { return acquirenteId; }
    public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }
}
