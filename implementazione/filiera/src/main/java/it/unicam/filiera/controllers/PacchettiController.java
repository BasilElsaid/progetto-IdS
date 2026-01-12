package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreatePacchettoRequest;
import it.unicam.filiera.prodotto.Pacchetto;
import it.unicam.filiera.services.PacchettiService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/pacchetti")
public class PacchettiController {

    private final PacchettiService service;

    public PacchettiController(PacchettiService service) {
        this.service = service;
    }

    @PostMapping
    public Pacchetto crea(@RequestBody CreatePacchettoRequest request) {
        return service.crea(request);
    }
}
