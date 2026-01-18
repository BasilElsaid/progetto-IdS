package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.enums.MetodoPagamento;

public class PagaOrdineRequest {
    private MetodoPagamento metodo;

    public MetodoPagamento getMetodo() { return metodo; }
    public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }
}
