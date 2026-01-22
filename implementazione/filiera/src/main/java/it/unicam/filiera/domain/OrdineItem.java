package it.unicam.filiera.domain;

import jakarta.persistence.*;

@Entity
public class OrdineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // riferimento a annuncio prodotto o pacchetto
    private Long annuncioId;
    private boolean isPacchetto;
    private int quantita;
    private double prezzoUnitario;

    @ManyToOne
    private Ordine ordine;

    public double getTotaleRiga() {
        return prezzoUnitario * quantita;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnnuncioId() {
        return annuncioId;
    }

    public void setAnnuncioId(Long annuncioId) {
        this.annuncioId = annuncioId;
    }

    public boolean isPacchetto() {
        return isPacchetto;
    }

    public void setPacchetto(boolean pacchetto) {
        isPacchetto = pacchetto;
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

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }}