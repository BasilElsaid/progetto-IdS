package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ACQUIRENTE")
public class Acquirente extends UtenteGenerico {


    public Acquirente() {
        setRuolo(Ruolo.ACQUIRENTE);
    }
}
