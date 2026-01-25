package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.response.SpedizioneResponse;
import it.unicam.filiera.services.SpedizioniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import it.unicam.filiera.dto.create.CreateSpedisciRequest;

@RestController
@RequestMapping("/api/spedizioni")
@Tag(name = "12 - Spedizioni", description = "Gestione Spedizioni degli ordini")
@PreAuthorize("hasAnyRole('CORRIERE','GESTORE_PIATTAFORMA')")
public class SpedizioniController {

    private final SpedizioniService service;

    public SpedizioniController(SpedizioniService service) {
        this.service = service;
    }

    @PostMapping("/{ordineId}/spedisci")
    public SpedizioneResponse spedisci(@PathVariable Long ordineId,
                                       @RequestBody CreateSpedisciRequest req) {
        return service.spedisci(ordineId, req.trackingCode());
    }

    @PostMapping("/{ordineId}/consegna")
    public SpedizioneResponse consegna(@PathVariable Long ordineId) {
        return service.consegna(ordineId);
    }
}