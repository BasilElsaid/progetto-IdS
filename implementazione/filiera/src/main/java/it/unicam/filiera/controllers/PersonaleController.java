package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreatePersonaleRequest;
import it.unicam.filiera.controllers.dto.UtenteResponse;
import it.unicam.filiera.services.PersonaleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale")
@Tag(name = "Personale", description = "Gestione Curatore, Animatore e Gestore Piattaforma")
public class PersonaleController {

    private final PersonaleService service;

    public PersonaleController(PersonaleService service) {
        this.service = service;
    }

    @PostMapping
    public UtenteResponse crea(@RequestBody CreatePersonaleRequest request) {
        return service.creaPersonale(request);
    }

    @GetMapping
    public List<UtenteResponse> lista() {
        return service.listaPersonale();
    }
}