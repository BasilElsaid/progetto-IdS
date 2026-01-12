package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.services.AcquirentiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/acquirenti")
public class AcquirentiController {

    private final AcquirentiService service;

    public AcquirentiController(AcquirentiService service) {
        this.service = service;
    }

    @PostMapping
    public Acquirente crea(@RequestBody CreateAcquirenteRequest request) {
        return service.crea(request);
    }
}
