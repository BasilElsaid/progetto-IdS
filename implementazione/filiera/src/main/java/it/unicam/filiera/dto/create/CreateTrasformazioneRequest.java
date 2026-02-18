package it.unicam.filiera.dto.create;

public record CreateTrasformazioneRequest(
        Long inputId,
        String nuovoNomeOutput,
        String note
) {}