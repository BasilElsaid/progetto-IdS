package it.unicam.filiera.controllers;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.services.UtentiService;

@RestController
@RequestMapping("/api/users")
public class UtentiController {

    private final UtentiService utentiService;

    public UtentiController(UtentiService utentiService) {
        this.utentiService = utentiService;
    }

    @GetMapping("/me")
    public Acquirente me() {
        return utentiService.getCurrentUser();
    }

    @GetMapping("/{id}")
    public Acquirente byId(@PathVariable Long id) {
        return utentiService.getById(id);
    }
}
