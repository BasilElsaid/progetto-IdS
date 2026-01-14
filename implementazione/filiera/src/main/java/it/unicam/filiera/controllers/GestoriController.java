package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreatePersonaleRequest;
import it.unicam.filiera.models.GestorePiattaforma;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.GestorePiattaformaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestori")
@Tag(name = "Gestore Piattaforma", description = "Gestione Specifica di admin piattaforma (demo)")
public class GestoriController {

    private final GestorePiattaformaRepository repo;

    public GestoriController(GestorePiattaformaRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public GestorePiattaforma crea(@RequestBody CreatePersonaleRequest request) {
        GestorePiattaforma g = new GestorePiattaforma();
        g.setUsername(request.getUsername());
        g.setEmail(request.getEmail());
        g.setPassword(request.getPassword());
        g.setNome(request.getNome());
        g.setCognome(request.getCognome());
        g.setTelefono(request.getTelefono());
        g.setRuolo(Ruolo.GESTORE_PIATTAFORMA);
        return repo.save(g);
    }

    @GetMapping
    public List<GestorePiattaforma> lista() { return repo.findAll(); }

    @GetMapping("/{id}")
    public GestorePiattaforma get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
