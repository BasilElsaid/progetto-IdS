package it.unicam.filiera.models;

import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animatori")
public class Animatore extends Personale {

    @OneToMany(mappedBy = "animatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evento> eventi = new ArrayList<>();

    public List<Evento> getEventi() { return eventi; }

    public Animatore() {
        setRuolo(Ruolo.ANIMATORE);
    }

    public void aggiungiEvento(Evento evento) {
        eventi.add(evento);
        evento.setAnimatore(this);
    }

    public void rimuoviEvento(Evento evento) {
        eventi.remove(evento);
        evento.setAnimatore(null);
    }
}
