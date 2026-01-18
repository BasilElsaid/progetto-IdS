package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.enums.StatoPagamento;

import java.time.LocalDateTime;

public class PagamentoResponse {
    public Long id;
    public Long ordineId;
    public MetodoPagamento metodo;
    public StatoPagamento stato;
    public double importo;
    public LocalDateTime pagatoIl;

    public static PagamentoResponse from(PagamentoOrdine p) {
        PagamentoResponse r = new PagamentoResponse();
        r.id = p.getId();
        r.ordineId = p.getOrdine().getId();
        r.metodo = p.getMetodo();
        r.stato = p.getStato();
        r.importo = p.getImporto();
        r.pagatoIl = p.getPagatoIl();
        return r;
    }
}
