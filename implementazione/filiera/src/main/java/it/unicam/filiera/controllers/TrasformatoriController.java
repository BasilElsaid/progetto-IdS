package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.models.Trasformatore;
import it.unicam.filiera.repositories.TrasformatoreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trasformatori")
@Tag(name = "Trasformatori", description = "Gestione Specifica di Trasformatori (processi di trasformazione e tracciabilità)")
public class TrasformatoriController {

    private final TrasformatoreRepository repo;

    public TrasformatoriController(TrasformatoreRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Trasformatore> lista() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Trasformatore get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
