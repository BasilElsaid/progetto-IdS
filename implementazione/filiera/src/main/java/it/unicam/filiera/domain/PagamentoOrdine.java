package it.unicam.filiera.domain;

import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoPagamento;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamenti_ordine")
public class PagamentoOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Ordine ordine;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPagamento metodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPagamento stato = StatoPagamento.IN_ATTESA;

    @Column(nullable = false)
    private double importo;

    private LocalDateTime pagatoIl;
    private String transactionId;

    protected PagamentoOrdine() {
    }

    public PagamentoOrdine(Ordine ordine, MetodoPagamento metodo, double importo) {
        this.ordine = ordine;
        this.metodo = metodo;
        this.importo = importo;
        this.stato = StatoPagamento.IN_ATTESA;
    }

    public Long getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }



    public Ordine getOrdine() { return ordine; }
    public void setOrdine(Ordine ordine) { this.ordine = ordine; }

    public MetodoPagamento getMetodo() { return metodo; }
    public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }

    public StatoPagamento getStato() { return stato; }
    public void setStato(StatoPagamento stato) { this.stato = stato; }

    public double getImporto() { return importo; }
    public void setImporto(double importo) { this.importo = importo; }

    public LocalDateTime getPagatoIl() { return pagatoIl; }
    public void setPagatoIl(LocalDateTime pagatoIl) { this.pagatoIl = pagatoIl; }
}
