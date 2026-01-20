package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.create.CreateOrdineRequest;
import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;
import it.unicam.filiera.services.OrdiniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordini")
@PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
public class OrdiniController {

    private final OrdiniService service;

    public OrdiniController(OrdiniService service) {
        this.service = service;
    }

    @PostMapping
    public Ordine crea(@RequestBody CreateOrdineRequest request) {
        return service.creaOrdine(request.getAcquirenteId(), request.getPacchettoIds());
    }

    @GetMapping("/{id}")
    public Ordine get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<Ordine> all() {
        return service.all();
    }

    @GetMapping("/acquirente/{acquirenteId}")
    public List<Ordine> byAcquirente(@PathVariable Long acquirenteId) {
        return service.getByAcquirente(acquirenteId);
    }

    // PATCH stato: lo deve vedere anche Swagger se questo file è in src/main/java
    @PatchMapping("/{id}/stato")
    @PreAuthorize("hasRole('GESTORE_PIATTAFORMA')")
    public Ordine aggiornaStato(@PathVariable Long id, @RequestBody UpdateStatoReq req) {
        return service.aggiornaStato(id, req.getStato(), req.getTrackingCode());
    }

    public static class UpdateStatoReq {
        private StatoOrdine stato;
        private String trackingCode;

        public StatoOrdine getStato() { return stato; }
        public void setStato(StatoOrdine stato) { this.stato = stato; }

        public String getTrackingCode() { return trackingCode; }
        public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }
    }
}
