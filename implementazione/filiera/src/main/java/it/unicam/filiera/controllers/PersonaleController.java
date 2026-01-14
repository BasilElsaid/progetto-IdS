package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.services.PersonaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale")
@Tag(name = "Personale", description = "Endpoint per gestire Curatori, Animatori e Gestori Piattaforma")
public class PersonaleController {

    private final PersonaleService service;

    public PersonaleController(PersonaleService service) {
        this.service = service;
    }

    @GetMapping
    public List<UtenteResponse> listaPersonale() {
        return service.listaPersonale();
    }
}