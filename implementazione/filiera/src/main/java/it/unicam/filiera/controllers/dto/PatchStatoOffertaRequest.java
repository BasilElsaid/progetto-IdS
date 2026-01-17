package it.unicam.filiera.controllers.dto;

public class PatchStatoOffertaRequest {
    private Double prezzoVendita;
    private Integer disponibilita;
    private Boolean attiva;

    public Double getPrezzoVendita() { return prezzoVendita; }
    public void setPrezzoVendita(Double prezzoVendita) { this.prezzoVendita = prezzoVendita; }

    public Integer getDisponibilita() { return disponibilita; }
    public void setDisponibilita(Integer disponibilita) { this.disponibilita = disponibilita; }

    public Boolean getAttiva() { return attiva; }
    public void setAttiva(Boolean attiva) { this.attiva = attiva; }
}