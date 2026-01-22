package it.unicam.filiera.domain;

import it.unicam.filiera.models.Acquirente;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "recensioni",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ordine_id", "acquirente_id"})
)
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ordine ordine;

    @ManyToOne(optional = false)
    private Acquirente acquirente;

    @Column(nullable = false)
    private int voto; // 1..5

    @Column(length = 2000)
    private String testo;

    @Column(nullable = false)
    private LocalDateTime creataIl = LocalDateTime.now();

    protected Recensione() {}

    public Recensione(Ordine ordine, Acquirente acquirente, int voto, String testo) {
        this.ordine = ordine;
        this.acquirente = acquirente;
        this.voto = voto;
        this.testo = testo;
        this.creataIl = LocalDateTime.now();
    }

    // --- GETTERS / SETTERS ---
    public Long getId() { return id; }
    public Ordine getOrdine() { return ordine; }
    public Acquirente getAcquirente() { return acquirente; }
    public int getVoto() { return voto; }
    public String getTesto() { return testo; }
    public LocalDateTime getCreataIl() { return creataIl; }
}