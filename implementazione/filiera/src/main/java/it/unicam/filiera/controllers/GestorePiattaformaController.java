package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.repositories.GestorePiattaformaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale/gestore")
@Tag(name = "Gestore Piattaforma", description = "Gestione Specifica di admin piattaforma (demo)")
public class GestorePiattaformaController {

    private final GestorePiattaformaRepository repo;

    public GestorePiattaformaController(GestorePiattaformaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<GestorePiattaforma> lista() { return repo.findAll(); }
}
