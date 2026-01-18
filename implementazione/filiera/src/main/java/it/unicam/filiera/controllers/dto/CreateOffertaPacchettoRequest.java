package it.unicam.filiera.controllers.dto;

public class CreateOffertaPacchettoRequest {
    private Long pacchettoId;
    private double prezzoVendita;
    private int disponibilita;
    private boolean attiva;

    public Long getPacchettoId() { return pacchettoId; }
    public void setPacchettoId(Long pacchettoId) { this.pacchettoId = pacchettoId; }

    public double getPrezzoVendita() { return prezzoVendita; }
    public void setPrezzoVendita(double prezzoVendita) { this.prezzoVendita = prezzoVendita; }

    public int getDisponibilita() { return disponibilita; }
    public void setDisponibilita(int disponibilita) { this.disponibilita = disponibilita; }

    public boolean isAttiva() { return attiva; }
    public void setAttiva(boolean attiva) { this.attiva = attiva; }
}
