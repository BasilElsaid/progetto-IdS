package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trasformatori")
public class Trasformatore extends Azienda {

    private String laboratorio;

    public Trasformatore() {
        setRuolo(Ruolo.TRASFORMATORE);
    }

    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
}
