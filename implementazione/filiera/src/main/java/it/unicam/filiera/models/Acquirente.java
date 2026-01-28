package it.unicam.filiera.models;

import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("ACQUIRENTE")
public class Acquirente extends UtenteGenerico {

    @ManyToMany
    @JoinTable(
            name = "acquirente_prodotti",
            joinColumns = @JoinColumn(name = "acquirente_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodottiAcquistati = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "acquirente_pacchetti",
            joinColumns = @JoinColumn(name = "acquirente_id"),
            inverseJoinColumns = @JoinColumn(name = "pacchetto_id")
    )
    private List<Pacchetto> pacchettiAcquistati = new ArrayList<>();

    public Acquirente() {
        setRuolo(Ruolo.ACQUIRENTE);
    }

    public List<Prodotto> getProdottiAcquistati() {
        return prodottiAcquistati;
    }

    public void setProdottiAcquistati(List<Prodotto> prodottiAcquistati) {
        this.prodottiAcquistati = prodottiAcquistati;
    }

    public List<Pacchetto> getPacchettiAcquistati() {
        return pacchettiAcquistati;
    }

    public void setPacchettiAcquistati(List<Pacchetto> pacchettiAcquistati) {
        this.pacchettiAcquistati = pacchettiAcquistati;
    }
}