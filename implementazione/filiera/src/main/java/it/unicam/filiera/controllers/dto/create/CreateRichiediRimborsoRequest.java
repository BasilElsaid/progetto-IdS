package it.unicam.filiera.controllers.dto.create;

public class CreateRichiediRimborsoRequest {
    private Long acquirenteId;
    private Long ordineId;
    private String motivazione;

    public Long getAcquirenteId() { return acquirenteId; }
    public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

    public Long getOrdineId() { return ordineId; }
    public void setOrdineId(Long ordineId) { this.ordineId = ordineId; }

    public String getMotivazione() { return motivazione; }
    public void setMotivazione(String motivazione) { this.motivazione = motivazione; }
}