package it.unicam.filiera.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUtenteRequest(
        @NotBlank String username,
        @NotBlank String email
) {}
