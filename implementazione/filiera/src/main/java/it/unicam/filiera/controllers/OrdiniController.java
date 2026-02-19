package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.response.OrdineResponse;
import it.unicam.filiera.services.OrdiniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import it.unicam.filiera.dto.create.CreateOrdineItemRequest;

import java.util.List;

@RestController
@RequestMapping("/api/ordini")
@Tag(name = "11 - Ordini", description = "Gestione ordini")
@PreAuthorize("hasRole('ACQUIRENTE')")
public class OrdiniController {

    private final OrdiniService service;

    public OrdiniController(OrdiniService service) {
        this.service = service;
    }

    @PostMapping
    public OrdineResponse crea(@RequestBody List<CreateOrdineItemRequest> items) {
        return service.creaOrdine(items);
    }

    @PostMapping("/paga/{ordineId}")
    public OrdineResponse paga(@PathVariable Long ordineId) {
        return service.pagaOrdine(ordineId);
    }

    @DeleteMapping("/{ordineId}")
    public void elimina(@PathVariable Long ordineId) {
        service.eliminaOrdine(ordineId);
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
    @GetMapping
    public List<OrdineResponse> lista() {
        return service.getAllOrdini();
    }

}