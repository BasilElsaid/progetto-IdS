package it.unicam.filiera.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "curatori")
public class Curatore extends UtenteGenerico {
    private String ente;
}
