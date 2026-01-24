package it.unicam.filiera.dto.create;

public record CreateTrasformazioneRequest(
        Long processoId,
        Long trasformatoreId,
        Long inputId,
        Long outputId,
        Double quantitaInput,
        Double quantitaOutput,
        String note
) {}