package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.models.UtenteGenerico;

public class UtenteResponse {
    public Long id;
    public String username;
    public String email;
    public Ruolo ruolo;

    public static UtenteResponse from(UtenteGenerico u) {
        UtenteResponse r = new UtenteResponse();
        r.id = u.getId();
        r.username = u.getUsername();
        r.email = u.getEmail();
        r.ruolo = u.getRuolo();
        return r;
    }
}
