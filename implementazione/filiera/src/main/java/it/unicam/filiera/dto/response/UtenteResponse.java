package it.unicam.filiera.dto.response;

import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.models.UtenteGenerico;

public class UtenteResponse {
    public Long id;
    public String username;
    public Ruolo ruolo;

    public static UtenteResponse from(UtenteGenerico u) {
        UtenteResponse r = new UtenteResponse();
        r.id = u.getId();
        r.username = u.getUsername();
        r.ruolo = u.getRuolo();
        return r;
    }
}
