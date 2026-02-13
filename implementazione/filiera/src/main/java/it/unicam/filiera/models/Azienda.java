package it.unicam.filiera.models;

import it.unicam.filiera.utilities.CoordinateOSM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Azienda extends UtenteGenerico {

    @NotBlank(message = "Nome azienda obbligatorio")
    private String nomeAzienda;

    @NotBlank(message = "Sede obbligatoria")
    private String sede;

    @NotNull(message = "Coordinate obbligatorie")
    private CoordinateOSM coordinate;

    private String partitaIva;


    // ===== COSTRUTTORI =====
    public Azienda() {}

    // ===== GETTERS =====

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public String getSede() {
        return sede;
    }


    public String getPartitaIva() { return partitaIva; }

    // ===== SETTERS =====
    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public CoordinateOSM getCoordinate() { return coordinate; }

    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }

    public void setCoordinate(CoordinateOSM coordinate) { this.coordinate = coordinate; }
}
