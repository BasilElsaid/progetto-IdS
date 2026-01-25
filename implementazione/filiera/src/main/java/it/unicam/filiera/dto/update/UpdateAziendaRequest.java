package it.unicam.filiera.dto.update;

import it.unicam.filiera.dto.create.CreateCoordinateRequest;
import jakarta.validation.constraints.Email;

public record UpdateAziendaRequest(
        @Email String email,
        String password,
        String nomeAzienda,
        String partitaIva,
        String sede,
        String areaDistribuzione,
        String laboratorio,
        CreateCoordinateRequest coordinate
) {}