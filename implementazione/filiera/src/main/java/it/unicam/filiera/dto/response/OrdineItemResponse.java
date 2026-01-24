package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.OrdineItem;

public record OrdineItemResponse(
        Long id,
        Long annuncioId,
        int quantita,
        double prezzoUnitario,
        boolean pacchetto
) {
    public static OrdineItemResponse from(OrdineItem item) {
        return new OrdineItemResponse(
                item.getId(),
                item.getAnnuncioId(),
                item.getQuantita(),
                item.getPrezzoUnitario(),
                item.isPacchetto()
        );
    }
}