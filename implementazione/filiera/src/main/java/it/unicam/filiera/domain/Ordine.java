package it.unicam.filiera.domain;

import it.unicam.filiera.enums.StatoOrdine;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordini")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // chi compra
    @ManyToOne(optional = false)
    private Acquirente acquirente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato = StatoOrdine.CREATO;

    @Column(nullable = false)
    private LocalDateTime dataOra = LocalDateTime.now();

    // totale ordine
    @Column(nullable = false)
    private double importoTotale = 0.0;

    // prodotti comprati (semplice: senza riga ordine)
    @ManyToMany
    @JoinTable(
            name = "ordine_prodotti",
            joinColumns = @JoinColumn(name = "ordine_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti = new ArrayList<>();

    public Ordine() {
    }

    public Ordine(Acquirente acquirente) {
        this.acquirente = acquirente;
        this.stato = StatoOrdine.CREATO;
        this.dataOra = LocalDateTime.now();
    }

    // ===== GET/SET =====

    public Long getId() {
        return id;
    }

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(Acquirente acquirente) {
        this.acquirente = acquirente;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public double getImportoTotale() {
        return importoTotale;
    }

    public void setImportoTotale(double importoTotale) {
        this.importoTotale = importoTotale;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    // helper
    public void aggiungiProdotto(Prodotto p) {
        this.prodotti.add(p);
    }
}
