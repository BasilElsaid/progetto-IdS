package it.unicam.filiera.dto.update;

public record UpdatePersonaleRequest(
        String email,
        String password,
        String nome,
        String cognome,
        String telefono
) {}