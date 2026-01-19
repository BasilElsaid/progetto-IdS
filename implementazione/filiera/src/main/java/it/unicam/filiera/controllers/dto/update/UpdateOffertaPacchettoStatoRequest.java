package it.unicam.filiera.controllers.dto.update;

public class UpdateOffertaPacchettoStatoRequest {

    private Boolean attiva;
    private Integer disponibilita;

    public UpdateOffertaPacchettoStatoRequest() {
    }

    public Boolean getAttiva() {
        return attiva;
    }

    public void setAttiva(Boolean attiva) {
        this.attiva = attiva;
    }

    public Integer getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Integer disponibilita) {
        this.disponibilita = disponibilita;
    }
}
