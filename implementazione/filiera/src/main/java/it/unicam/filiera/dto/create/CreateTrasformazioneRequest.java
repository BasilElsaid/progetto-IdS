package it.unicam.filiera.dto.create;

public record CreateTrasformazioneRequest(
        Long inputId,
        Long trasformatoreId,
        String nuovoNomeOutput,
        String note
) {}