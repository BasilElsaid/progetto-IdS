package it.unicam.filiera.domain;

import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoPagamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamenti_ordine")
public class PagamentoOrdine {

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
    private LocalDateTime dataOra;

    protected PagamentoOrdine() {}

    public PagamentoOrdine(Ordine ordine, MetodoPagamento metodo, double importo) {
        this.ordine = ordine;
        this.metodo = metodo;
        this.importo = importo;
        this.stato = StatoPagamento.IN_ATTESA;
        this.transactionId = UUID.randomUUID().toString();
        this.dataOra = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Ordine getOrdine() { return ordine; }
    public MetodoPagamento getMetodo() { return metodo; }
    public StatoPagamento getStato() { return stato; }
    public String getTransactionId() { return transactionId; }
    public double getImporto() { return importo; }
    public LocalDateTime getDataOra() { return dataOra; }

    public void setStato(StatoPagamento stato) { this.stato = stato; }
}
