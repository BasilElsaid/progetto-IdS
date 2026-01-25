package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.response.OrdineResponse;
import it.unicam.filiera.enums.MetodoPagamento;
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
    public OrdineResponse crea(@RequestBody List<CreateOrdineItemRequest> items,
                               @RequestParam Long acquirenteId) {
        return service.creaOrdine(acquirenteId, items);
    }

    @PostMapping("/{id}/paga")
    public OrdineResponse paga(@PathVariable Long id,
                               @RequestParam Long acquirenteId,
                               @RequestParam boolean pacchetto,
                               @RequestParam MetodoPagamento metodo) {
        return service.pagaOrdine(acquirenteId, id, pacchetto, metodo);
    }

    @GetMapping("/{id}")
    public OrdineResponse get(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<OrdineResponse> all() {
        return service.all();
    }

    @GetMapping("/acquirente/{acquirenteId}")
    public List<OrdineResponse> byAcquirente(@PathVariable Long acquirenteId) {
        return service.getByAcquirente(acquirenteId);
    }
}