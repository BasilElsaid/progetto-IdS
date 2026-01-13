package it.unicam.filiera.models;

/**
 * Classe ponte per ruoli "staff" (curatore/animatore/gestore/distributore).
 * Non è Entity: le Entity vere restano le sottoclassi.
 */
public abstract class Personale extends UtenteGenerico {
    public Personale() { super(); }
}
