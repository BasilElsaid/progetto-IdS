package it.unicam.filiera.dto.create;

public record CreateOrdineItemRequest(
        Long annuncioId,
        int quantita,
        boolean pacchetto
) {}