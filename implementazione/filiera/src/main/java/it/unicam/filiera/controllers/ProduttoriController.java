package it.unicam.filiera.controllers;

import it.unicam.filiera.models.Produttore;
import it.unicam.filiera.repositories.ProduttoreRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produttori")
@Tag(name = "Produttori", description = "Gestione Specifica di Produttori")
public class ProduttoriController {

    private final ProduttoreRepository repo;

    public ProduttoriController(ProduttoreRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Produttore> lista() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Produttore get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }
}