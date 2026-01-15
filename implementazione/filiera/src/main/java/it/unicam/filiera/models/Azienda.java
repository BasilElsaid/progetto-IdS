package it.unicam.filiera.models;

import it.unicam.filiera.utilities.CoordinateOSM;
import jakarta.persistence.*;

@Entity
public class Azienda extends UtenteGenerico{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeAzienda;

    private String sede;

    @Embedded
    private CoordinateOSM coordinate;

    private String partitaIva;


    // ===== COSTRUTTORI =====
    public Azienda() {}

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public String getSede() {
        return sede;
    }


    public String getPartitaIva() { return partitaIva; }

    // ===== SETTERS =====
    public void setId(Long id) {
        this.id = id;
    }

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
