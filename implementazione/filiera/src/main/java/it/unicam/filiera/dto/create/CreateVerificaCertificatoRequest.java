package it.unicam.filiera.dto.create;

public record CreateVerificaCertificatoRequest(
        Boolean approvato,
        String commento
) {}