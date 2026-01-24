package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProdottoRequest(
        @NotBlank String nome,
        @NotBlank String categoria
) {}
