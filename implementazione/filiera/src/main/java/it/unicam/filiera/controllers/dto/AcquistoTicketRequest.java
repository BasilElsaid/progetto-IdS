package it.unicam.filiera.controllers.dto;

public class AcquistoTicketRequest {
    private Long acquirenteId;
    private Integer quantita;

    public Long getAcquirenteId() { return acquirenteId; }
    public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

    public Integer getQuantita() { return quantita; }
    public void setQuantita(Integer quantita) { this.quantita = quantita; }
}