package it.unicam.filiera.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "righe_ordine_marketplace")
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

    protected RigaOrdineMarketplace() {}

    public RigaOrdineMarketplace(Ordine ordine, AnnuncioMarketplace annuncio, int quantita, double prezzoUnitarioAlMomento) {
        this.ordine = ordine;
        this.annuncio = annuncio;
        this.quantita = quantita;
        this.prezzoUnitarioAlMomento = prezzoUnitarioAlMomento;
        this.subtotale = prezzoUnitarioAlMomento * quantita;
    }

    public Long getId() { return id; }
    public Ordine getOrdine() { return ordine; }
    public AnnuncioMarketplace getAnnuncio() { return annuncio; }
    public int getQuantita() { return quantita; }
    public double getPrezzoUnitarioAlMomento() { return prezzoUnitarioAlMomento; }
    public double getSubtotale() { return subtotale; }
}
