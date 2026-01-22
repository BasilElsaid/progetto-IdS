package it.unicam.filiera.domain;

import it.unicam.filiera.models.Acquirente;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "recensioni",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ordine_id", "item_id", "acquirente_id"})
)
public class Recensione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ordine ordine;

    @ManyToOne
    private Prodotto prodotto; // se recensione su prodotto singolo, null se pacchetto

    @ManyToOne(optional = false)
    private Acquirente acquirente;

    @Column(nullable = false)
    private Long itemId; // id generico prodotto o pacchetto

    @Column(nullable = false)
    private boolean pacchetto = false;

    @Column(nullable = false)
    private int voto; // 1..5

    @Column(length = 2000)
    private String testo;

    @Column(nullable = false)
    private LocalDateTime creataIl = LocalDateTime.now();

    protected Recensione() {}

    public Recensione(Ordine ordine, Prodotto prodotto, Acquirente acquirente, int voto, String testo) {
        this.ordine = ordine;
        this.prodotto = prodotto;
        this.acquirente = acquirente;
        this.voto = voto;
        this.testo = testo;
        this.creataIl = LocalDateTime.now();
    }

    // --- GETTERS / SETTERS ---

    public Long getId() { return id; }
    public Ordine getOrdine() { return ordine; }
    public Prodotto getProdotto() { return prodotto; }
    public Acquirente getAcquirente() { return acquirente; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public boolean isPacchetto() { return pacchetto; }
    public void setPacchetto(boolean pacchetto) { this.pacchetto = pacchetto; }
    public int getVoto() { return voto; }
    public String getTesto() { return testo; }
    public LocalDateTime getCreataIl() { return creataIl; }
}