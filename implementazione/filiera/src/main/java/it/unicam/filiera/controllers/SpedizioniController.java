package it.unicam.filiera.controllers;

import it.unicam.filiera.services.SpedizioniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spedizioni")
public class SpedizioniController {

    private final SpedizioniService service;

    public SpedizioniController(SpedizioniService service) {
        this.service = service;
    }

    @PostMapping("/{ordineId}/spedisci")
    @PreAuthorize("hasAnyRole('CORRIERE','GESTORE_PIATTAFORMA')")
    public String spedisci(@PathVariable Long ordineId, @RequestBody SpedisciReq req) {
        return service.spedisci(ordineId, req.getTrackingCode());
    }

    @PostMapping("/{ordineId}/consegna")
    @PreAuthorize("hasAnyRole('CORRIERE','GESTORE_PIATTAFORMA')")
    public String consegna(@PathVariable Long ordineId) {
        return service.consegna(ordineId);
    }

    public static class SpedisciReq {
        private String trackingCode;
        public String getTrackingCode() { return trackingCode; }
        public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }
    }
}