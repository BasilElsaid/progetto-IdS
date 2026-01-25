package it.unicam.filiera.dto.update;

import jakarta.validation.constraints.Email;

public record UpdateAcquirenteRequest(
        @Email String email,
        String password
) {}