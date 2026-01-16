package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GESTORE_PIATTAFORMA")
public class GestorePiattaforma extends Personale {

    public GestorePiattaforma() {
        setRuolo(Ruolo.GESTORE_PIATTAFORMA);
    }
}
