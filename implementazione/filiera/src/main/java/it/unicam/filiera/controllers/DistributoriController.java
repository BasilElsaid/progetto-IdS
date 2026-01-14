package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.DistributoreTipicitaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/distributori")
@Tag(name = "Distributori di Tipicità", description = "Gestione Specifica di Distributori (pacchetti, vendita tipicità, ecc.)")
public class DistributoriController {

    private final DistributoreTipicitaRepository repo;

    public DistributoriController(DistributoreTipicitaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<DistributoreTipicita> lista() { return repo.findAll(); }

    @GetMapping("/{id}")
    public DistributoreTipicita get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
