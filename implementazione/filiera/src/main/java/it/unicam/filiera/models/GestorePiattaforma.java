package it.unicam.filiera.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GESTORE_PIATTAFORMA")
public class GestorePiattaforma extends Personale {

    public GestorePiattaforma() {
        setRuolo(Ruolo.GESTORE_PIATTAFORMA);
    }
}
