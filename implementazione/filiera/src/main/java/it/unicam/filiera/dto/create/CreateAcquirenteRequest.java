package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.Email;

public record CreateAcquirenteRequest(
        String username,
        @Email String email,
        String password
) {}