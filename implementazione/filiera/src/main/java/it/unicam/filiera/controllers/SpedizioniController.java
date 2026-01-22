package it.unicam.filiera.controllers;

import it.unicam.filiera.services.SpedizioniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import it.unicam.filiera.controllers.dto.create.CreateSpedisciRequest;

@RestController
@RequestMapping("/api/spedizioni")
@PreAuthorize("hasAnyRole('CORRIERE','GESTORE_PIATTAFORMA')")
public class SpedizioniController {

    private final SpedizioniService service;

    public SpedizioniController(SpedizioniService service) {
        this.service = service;
    }

    @PostMapping("/{ordineId}/spedisci")
    public String spedisci(@PathVariable Long ordineId, @RequestBody CreateSpedisciRequest req) {
        return service.spedisci(ordineId, req.getTrackingCode());
    }

    @PostMapping("/{ordineId}/consegna")
    public String consegna(@PathVariable Long ordineId) {
        return service.consegna(ordineId);
    }
}