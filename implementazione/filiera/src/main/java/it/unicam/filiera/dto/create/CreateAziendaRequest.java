package it.unicam.filiera.dto.create;

import it.unicam.filiera.enums.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAziendaRequest(
        @NotNull Ruolo ruolo,
        @NotBlank String username,
        @Email String email,
        @NotBlank String password,
        String nomeAzienda,
        String partitaIva,
        String sede,
        String areaDistribuzione,
        String laboratorio,
        CreateCoordinateRequest coordinate
) {}