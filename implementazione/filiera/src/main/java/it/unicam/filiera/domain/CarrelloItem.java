package it.unicam.filiera.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "carrello_items")
public class CarrelloItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Carrello carrello;

    @ManyToOne(optional = false)
    private AnnuncioProdotto annuncio;

    @ManyToOne(optional = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    @Column(nullable = false)
    private double prezzoUnitario;

    @Column(nullable = false)
    private LocalDateTime aggiuntoIl = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime scadeIl;

    protected CarrelloItem() {
    }

    public CarrelloItem(AnnuncioProdotto annuncio, int quantita, double prezzoUnitario, LocalDateTime scadeIl) {
        this.annuncio = annuncio;
        this.prodotto = annuncio.getProdotto();
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
        this.scadeIl = scadeIl;
        this.aggiuntoIl = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public AnnuncioProdotto getAnnuncio() {
        return annuncio;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzoUnitario() {
        return prezzoUnitario;
    }

    public void setPrezzoUnitario(double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public LocalDateTime getAggiuntoIl() {
        return aggiuntoIl;
    }

    public LocalDateTime getScadeIl() {
        return scadeIl;
    }

    public void setScadeIl(LocalDateTime scadeIl) {
        this.scadeIl = scadeIl;
    }

    public boolean isScaduto(LocalDateTime now) {
        return scadeIl.isBefore(now) || scadeIl.isEqual(now);
    }

    public double getTotaleRiga() {
        return prezzoUnitario * quantita;
    }
}
