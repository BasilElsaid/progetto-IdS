package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.models.Curatore;
import it.unicam.filiera.repositories.CuratoreRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale/curatore")
@Tag(name = "Curatore", description = "Gestione Specifica del Curatore (approvazione contenuti, certificazioni, ecc.)")
@PreAuthorize("hasAnyRole('CURATORE', 'GESTORE_PIATTAFORMA')")
public class CuratoreController {

    private final CuratoreRepository repo;

    public CuratoreController(CuratoreRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Curatore> lista() { return repo.findAll(); }
}
