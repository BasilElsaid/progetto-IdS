package it.unicam.filiera.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "produttori")
public class Produttore extends Azienda {

    public Produttore() {}

}
