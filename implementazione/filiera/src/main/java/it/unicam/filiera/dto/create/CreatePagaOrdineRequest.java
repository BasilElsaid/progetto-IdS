package it.unicam.filiera.dto.create;

import it.unicam.filiera.enums.MetodoPagamento;

public record CreatePagaOrdineRequest (
        Long acquirenteId,
        boolean pacchetto,
        MetodoPagamento metodo
){}