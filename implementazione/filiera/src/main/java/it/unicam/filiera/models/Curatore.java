package it.unicam.filiera.models;

import jakarta.persistence.*;

@Entity
@Table(name = "curatori")
public class Curatore extends Personale {

    private String ente;
}