package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CORRIERE")
public class Corriere extends Personale {
    public Corriere() {
        setRuolo(Ruolo.CORRIERE);
    }
}
