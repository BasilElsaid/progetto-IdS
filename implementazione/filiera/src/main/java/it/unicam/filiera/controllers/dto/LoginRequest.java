package it.unicam.filiera.controllers.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username
) {}
