package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.NotBlank;

public record CreateLoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}