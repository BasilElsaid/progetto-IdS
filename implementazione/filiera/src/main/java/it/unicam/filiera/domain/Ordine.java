package it.unicam.filiera.domain;

import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.models.Acquirente;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Acquirente acquirente;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordine", orphanRemoval = true)
    private List<OrdineItem> items = new ArrayList<>();

    private double totale;

    @Enumerated(EnumType.STRING)
    private StatoOrdine stato;

    private LocalDateTime dataCreazione;
    private LocalDateTime dataPagamento;
    private LocalDateTime dataSpedizione;
    private LocalDateTime dataConsegna;

    private String trackingCode;

    // --- Utility ---
    public void aggiungiItem(OrdineItem item) {
        item.setOrdine(this);
        items.add(item);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acquirente getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(Acquirente acquirente) {
        this.acquirente = acquirente;
    }

    public List<OrdineItem> getItems() {
        return items;
    }

    public void setItems(List<OrdineItem> items) {
        this.items = items;
    }

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDateTime getDataSpedizione() {
        return dataSpedizione;
    }

    public void setDataSpedizione(LocalDateTime dataSpedizione) {
        this.dataSpedizione = dataSpedizione;
    }

    public LocalDateTime getDataConsegna() {
        return dataConsegna;
    }

    public void setDataConsegna(LocalDateTime dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }
}