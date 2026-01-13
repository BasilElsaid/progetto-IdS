package it.unicam.filiera.models;

import jakarta.persistence.*;

@Entity
@Table(name = "curatori")
@DiscriminatorValue("CURATORE")
@PrimaryKeyJoinColumn(name = "id") // PK = FK verso utenti.id
public class Curatore extends UtenteGenerico {

    private String ente;

    public String getEnte() { return ente; }
    public void setEnte(String ente) { this.ente = ente; }
}
