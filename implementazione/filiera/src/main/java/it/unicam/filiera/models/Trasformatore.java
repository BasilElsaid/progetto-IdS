package it.unicam.filiera.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trasformatori")
public class Trasformatore extends UtenteGenerico {

    private String laboratorio;

    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }
}
