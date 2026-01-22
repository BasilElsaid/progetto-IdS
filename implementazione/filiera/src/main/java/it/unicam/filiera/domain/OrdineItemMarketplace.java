package it.unicam.filiera.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "ordine_items_marketplace")
public class OrdineItemMarketplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ordine ordine;

    @ManyToOne(optional = false)
    private AnnuncioProdotto annuncio;

    @ManyToOne(optional = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private int quantita;

    @Column(nullable = false)
    private double prezzoUnitario;

    protected OrdineItemMarketplace() {
    }

    public OrdineItemMarketplace(AnnuncioProdotto annuncio, int quantita, double prezzoUnitario) {
        this.annuncio = annuncio;
        this.prodotto = annuncio.getProdotto();
        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
    }

    public Long getId() {
        return id;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
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

    public double getTotaleRiga() {
        return prezzoUnitario * quantita;
    }
}
