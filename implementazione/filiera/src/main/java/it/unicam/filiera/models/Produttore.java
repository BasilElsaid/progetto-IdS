package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "produttori")
public class Produttore extends Azienda {

    public Produttore() {
        setRuolo(Ruolo.PRODUTTORE);
    }

}
