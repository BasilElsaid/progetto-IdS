package it.unicam.filiera.dto.response;

import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.models.UtenteGenerico;

public record UtenteResponse(
        Long id,
        String username,
        Ruolo ruolo
) {
    public static UtenteResponse from(UtenteGenerico u) {
        return new UtenteResponse(u.getId(), u.getUsername(), u.getRuolo());
    }
}