package it.unicam.filiera.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.models.TipoAzienda;
import it.unicam.filiera.services.AziendeService;

@RestController
@RequestMapping("/api/aziende")
public class AziendeController {

    private final AziendeService service;

    public AziendeController(AziendeService service) {
        this.service = service;
    }

    // POST /api/aziende
    @PostMapping
    public Azienda create(@RequestBody Azienda azienda) {
        return service.create(azienda);
    }

    // GET /api/aziende/{id}
    @GetMapping("/{id}")
    public Azienda get(@PathVariable Long id) {
        return service.get(id);
    }

    // GET /api/aziende?tipo=AGRICOLA
    @GetMapping
    public List<Azienda> byTipo(@RequestParam(required = false) TipoAzienda tipo) {
        if (tipo == null) {
            // fallback semplice (tutte)
            return service.byTipo(TipoAzienda.PRODUTTORE);
        }
        return service.byTipo(tipo);
    }

    // PATCH /api/aziende/{id}
    @PatchMapping("/{id}")
    public Azienda update(@PathVariable Long id, @RequestBody Azienda azienda) {
        return service.update(id, azienda);
    }
}
