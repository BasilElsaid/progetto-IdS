package it.unicam.filiera.controllers.dto;

public class AcquistaTicketRequest {
    private Long acquirenteId;
    private int quantita;

    public Long getAcquirenteId() { return acquirenteId; }
    public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }
}