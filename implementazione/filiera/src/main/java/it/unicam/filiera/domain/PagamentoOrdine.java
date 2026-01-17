package it.unicam.filiera.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import it.unicam.filiera.domain.Ordine;

@Entity
public class PagamentoOrdine {
    public enum MetodoPagamento { CARD, CASH, SEPA }
    public enum StatoPagamento { IN_ATTESA, OK, FALLITO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Ordine ordine;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodo;

    @Enumerated(EnumType.STRING)
    private StatoPagamento stato;

    private String transactionId;
    private double importo;
    private LocalDateTime dataOra = LocalDateTime.now();

    public PagamentoOrdine() {}

    public Long getId() { return id; }

    public Ordine getOrdine() { return ordine; }
    public void setOrdine(Ordine ordine) { this.ordine = ordine; }

    public MetodoPagamento getMetodo() { return metodo; }
    public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }

    public StatoPagamento getStato() { return stato; }
    public void setStato(StatoPagamento stato) { this.stato = stato; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
}