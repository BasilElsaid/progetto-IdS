package it.unicam.filiera.controllers.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateProdottoRequest(
        @NotBlank String nome,
        @NotBlank String descrizione,
        @NotBlank String origine,
        @NotBlank String lotto,
        @Min(0) double prezzo,
        @NotNull Long produttoreId
) {}
