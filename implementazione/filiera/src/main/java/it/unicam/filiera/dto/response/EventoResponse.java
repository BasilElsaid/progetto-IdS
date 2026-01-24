package it.unicam.filiera.dto.response;

import java.time.LocalDateTime;

public record EventoResponse(
        Long id,
        String nome,
        LocalDateTime dataOra,
        Double prezzo,
        String tipo,
        int posti
) {}