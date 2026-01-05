package it.unicam.filiera.ordine;

import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.prodotto.Pacchetto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataOra = LocalDateTime.now();

    private double totale;

    @ManyToOne
    private Acquirente acquirente;

    @ManyToMany
    private List<Pacchetto> pacchetti;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato = StatoOrdine.CREATO;

    public Ordine() {} // richiesto da JPA/Hibernate :contentReference[oaicite:5]{index=5}

    public Long getId() { return id; }
    public LocalDateTime getDataOra() { return dataOra; }
    public double getTotale() { return totale; }
    public Acquirente getAcquirente() { return acquirente; }
    public List<Pacchetto> getPacchetti() { return pacchetti; }
    public StatoOrdine getStato() { return stato; }

    public void setTotale(double totale) { this.totale = totale; }
    public void setAcquirente(Acquirente acquirente) { this.acquirente = acquirente; }
    public void setPacchetti(List<Pacchetto> pacchetti) { this.pacchetti = pacchetti; }
    public void setStato(StatoOrdine stato) { this.stato = stato; }
}
