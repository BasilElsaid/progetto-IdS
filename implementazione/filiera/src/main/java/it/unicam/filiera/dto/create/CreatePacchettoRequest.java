package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreatePacchettoRequest(
        @NotBlank(message = "Il nome non può essere vuoto") String nome,
        @NotEmpty(message = "Il pacchetto deve contenere almeno un prodotto") List<Long> prodottiIds
) {}