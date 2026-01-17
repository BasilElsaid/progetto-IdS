package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoPagamento;

import java.time.LocalDateTime;

public class PagamentoResponse {
    private Long id;
    private Long ordineId;
    private MetodoPagamento metodo;
    private StatoPagamento stato;
    private String transactionId;
    private double importo;
    private LocalDateTime dataOra;

    public static PagamentoResponse from(PagamentoOrdine p) {
        PagamentoResponse r = new PagamentoResponse();
        r.id = p.getId();
        r.ordineId = p.getOrdine().getId();
        r.metodo = p.getMetodo();
        r.stato = p.getStato();
        r.transactionId = p.getTransactionId();
        r.importo = p.getImporto();
        r.dataOra = p.getDataOra();
        return r;
    }

    public Long getId() { return id; }
    public Long getOrdineId() { return ordineId; }
    public MetodoPagamento getMetodo() { return metodo; }
    public StatoPagamento getStato() { return stato; }
    public String getTransactionId() { return transactionId; }
    public double getImporto() { return importo; }
    public LocalDateTime getDataOra() { return dataOra; }
}