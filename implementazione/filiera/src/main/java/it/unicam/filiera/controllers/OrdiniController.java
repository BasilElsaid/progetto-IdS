package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateOrdineRequest;
import it.unicam.filiera.ordine.Ordine;
import it.unicam.filiera.services.OrdiniService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordini")
public class OrdiniController {

    private final OrdiniService service;

    public OrdiniController(OrdiniService service) {
        this.service = service;
    }

    @PostMapping
    public Ordine crea(@RequestBody CreateOrdineRequest request) {
        return service.creaOrdine(
                request.getAcquirenteId(),
                request.getPacchettoIds()
        );
    }

    @GetMapping("/{id}")
    public Ordine get(@PathVariable Long id) {
        return service.getById(id);
    }
}
