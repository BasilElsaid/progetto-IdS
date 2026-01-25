package it.unicam.filiera.dto.update;

public record UpdateCertificatoRequest(
        String azienda,
        String origineMateriaPrima,
        String processo,
        String impianto
) {}