package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreateAcquirenteRequest;
import it.unicam.filiera.models.Curatore;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.CuratoreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curatori")
@Tag(name = "Curatori", description = "Gestione Specifica di Curatori (approvazione contenuti, certificazioni, ecc.)")
public class CuratoriController {

    private final CuratoreRepository repo;

    public CuratoriController(CuratoreRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Curatore crea(@RequestBody CreateAcquirenteRequest request) {
        Curatore c = new Curatore();
        c.setUsername(request.getUsername());
        c.setEmail(request.getEmail());
        c.setPassword(request.getPassword());
        c.setRuolo(Ruolo.CURATORE);
        return repo.save(c);
    }

    @GetMapping
    public List<Curatore> lista() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Curatore get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
