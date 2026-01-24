package it.unicam.filiera.dto.create;

import it.unicam.filiera.enums.TipoCertificatore;
import jakarta.validation.constraints.NotNull;

public record CreateCertificatoRequest(
        @NotNull TipoCertificatore tipo,
        @NotNull Long prodottoId,
        String azienda,
        String origineMateriaPrima,
        String processo,
        String impianto
) {}