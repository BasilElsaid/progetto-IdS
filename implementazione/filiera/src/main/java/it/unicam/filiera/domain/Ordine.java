package it.unicam.filiera.domain;

import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.models.Acquirente;
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

    @ManyToOne(optional = false)
    private Acquirente acquirente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoOrdine stato = StatoOrdine.IN_ATTESA_PAGAMENTO;

    @Column(nullable = false)
    private LocalDateTime dataOra = LocalDateTime.now();

    @Column(nullable = false)
    private double importoTotale = 0.0;

    // Tracking / spedizione / consegna
    private LocalDateTime dataStimataConsegnaDa;
    private LocalDateTime dataStimataConsegnaA;
    private LocalDateTime dataSpedizione;
    private LocalDateTime dataConsegna;
    @Column(length = 64)
    private String trackingCode;

    // Righe marketplace (con quantità)
    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdineItemMarketplace> itemsMarketplace = new ArrayList<>();

    // Per ordini vecchi (mantengo compatibilità)
    @ManyToMany
    @JoinTable(
            name = "ordine_prodotti",
            joinColumns = @JoinColumn(name = "ordine_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    private List<Prodotto> prodotti = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "ordine_pacchetti",
            joinColumns = @JoinColumn(name = "ordine_id"),
            inverseJoinColumns = @JoinColumn(name = "pacchetto_id")
    )
    private List<Pacchetto> pacchetti = new ArrayList<>();

    public Ordine() { }

    public Ordine(Acquirente acquirente) {
        this.acquirente = acquirente;
        this.dataOra = LocalDateTime.now();
        this.stato = StatoOrdine.IN_ATTESA_PAGAMENTO;
    }

    public Long getId() { return id; }

    public Acquirente getAcquirente() { return acquirente; }
    public void setAcquirente(Acquirente acquirente) { this.acquirente = acquirente; }

    public StatoOrdine getStato() { return stato; }
    public void setStato(StatoOrdine stato) { this.stato = stato; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public double getImportoTotale() { return importoTotale; }
    public void setImportoTotale(double importoTotale) { this.importoTotale = importoTotale; }

    public double getTotale() { return importoTotale; }
    public void setTotale(double totale) { this.importoTotale = totale; }

    public LocalDateTime getDataStimataConsegnaDa() { return dataStimataConsegnaDa; }
    public void setDataStimataConsegnaDa(LocalDateTime v) { this.dataStimataConsegnaDa = v; }

    public LocalDateTime getDataStimataConsegnaA() { return dataStimataConsegnaA; }
    public void setDataStimataConsegnaA(LocalDateTime v) { this.dataStimataConsegnaA = v; }

    public LocalDateTime getDataSpedizione() { return dataSpedizione; }
    public void setDataSpedizione(LocalDateTime v) { this.dataSpedizione = v; }

    public LocalDateTime getDataConsegna() { return dataConsegna; }
    public void setDataConsegna(LocalDateTime v) { this.dataConsegna = v; }

    public String getTrackingCode() { return trackingCode; }
    public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }

    public List<OrdineItemMarketplace> getItemsMarketplace() { return itemsMarketplace; }
    public void setItemsMarketplace(List<OrdineItemMarketplace> itemsMarketplace) { this.itemsMarketplace = itemsMarketplace; }

    public List<Prodotto> getProdotti() { return prodotti; }
    public void setProdotti(List<Prodotto> prodotti) { this.prodotti = prodotti; }

    public List<Pacchetto> getPacchetti() { return pacchetti; }
    public void setPacchetti(List<Pacchetto> pacchetti) { this.pacchetti = pacchetti; }

    public void aggiungiItemMarketplace(OrdineItemMarketplace item) {
        item.setOrdine(this);
        this.itemsMarketplace.add(item);
    }
}
