package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.*;

@Entity
@Table(name = "curatori")
public class Curatore extends Personale {

    public Curatore() {
        setRuolo(Ruolo.CURATORE);
    }

    private String ente;
}