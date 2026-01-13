package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.services.UtentiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Utenti & Ruoli", description = "Endpoint DEMO per mostrare tutti i ruoli. UtenteGenerico (guest) non ha login e non è persistito.")
public class UtentiController {

    private final UtentiService service;

    public UtentiController(UtentiService service) {
        this.service = service;
    }

    @GetMapping
    public List<UtenteResponse> listaTutti() {
        return service.listaTutti();
    }
}
