package it.unicam.filiera.controllers;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.services.ProdottiService;

@RestController
@RequestMapping("/api/prodotti")
public class ProdottiController {

    private final ProdottiService service;

    public ProdottiController(ProdottiService service) {
        this.service = service;
    }

    @PostMapping
    public Prodotto crea(@RequestBody Prodotto p) {
        return service.crea(p);
    }

    @GetMapping("/{id}")
    public Prodotto get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public List<Prodotto> all() {
        return service.all();
    }
}
