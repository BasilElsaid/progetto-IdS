package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.repositories.AnimatoreRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale/animatore")
@Tag(name = "Animatore", description = "Gestione Specifica del Animatore (eventi, fiere, tour, degustazioni)")
@PreAuthorize("hasAnyRole('ANIMATORE', 'GESTORE_PIATTAFORMA')")
public class AnimatoreController {

    private final AnimatoreRepository repo;

    public AnimatoreController(AnimatoreRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Animatore> lista() { return repo.findAll(); }
}
