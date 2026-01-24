package it.unicam.filiera.dto.create;

import java.time.LocalDateTime;

public record CreateEventoRequest(
        String nome,
        LocalDateTime dataOra,
        Double prezzo,
        String tipo,
        Integer posti
) {}