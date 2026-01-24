package it.unicam.filiera.dto.response;

public record EventoStatsResponse(
        Long eventoId,
        int postiTotali,
        int postiResidui,
        long ticketsVenduti,
        long checkInFatti
) {}