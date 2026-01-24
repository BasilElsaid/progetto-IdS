package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrdineResponse(
        Long id,
        Long acquirenteId,
        StatoOrdine stato,
        LocalDateTime dataCreazione,
        LocalDateTime dataPagamento,
        double totale,
        List<OrdineItemResponse> items
) {

    public static OrdineResponse from(Ordine ordine) {
        return new OrdineResponse(
                ordine.getId(),
                ordine.getAcquirente().getId(),
                ordine.getStato(),
                ordine.getDataCreazione(),
                ordine.getDataPagamento(),
                ordine.getTotale(),
                ordine.getItems().stream()
                        .map(OrdineItemResponse::from)
                        .collect(Collectors.toList())
        );
    }
}