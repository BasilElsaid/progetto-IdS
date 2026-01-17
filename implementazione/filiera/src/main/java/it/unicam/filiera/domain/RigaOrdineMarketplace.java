package it.unicam.filiera.domain;

import jakarta.persistence.*;
import it.unicam.filiera.domain.Ordine;

@Entity
public class RigaOrdineMarketplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ordine ordine;

    @ManyToOne(optional = false)
    private AnnuncioMarketplace annuncio;

    private int quantita;
    private double prezzoUnitarioAlMomento;
    private double subtotale;

    public RigaOrdineMarketplace() {}

    public Long getId() { return id; }

    public Ordine getOrdine() { return ordine; }
    public void setOrdine(Ordine ordine) { this.ordine = ordine; }

    public AnnuncioMarketplace getAnnuncio() { return annuncio; }
    public void setAnnuncio(AnnuncioMarketplace annuncio) { this.annuncio = annuncio; }

    public int getQuantita() { return quantita; }
    public void setQuantita(int quantita) { this.quantita = quantita; }

    public double getPrezzoUnitarioAlMomento() { return prezzoUnitarioAlMomento; }
    public void setPrezzoUnitarioAlMomento(double prezzoUnitarioAlMomento) { this.prezzoUnitarioAlMomento = prezzoUnitarioAlMomento; }

    public double getSubtotale() { return subtotale; }
    public void setSubtotale(double subtotale) { this.subtotale = subtotale; }
}