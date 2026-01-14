package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreatePersonaleRequest;
import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.models.Ruolo;
import it.unicam.filiera.repositories.AnimatoreRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animatori")
@Tag(name = "Animatori", description = "Gestione Specifica di Animatori (eventi, fiere, tour, degustazioni)")
public class AnimatoriController {

    private final AnimatoreRepository repo;

    public AnimatoriController(AnimatoreRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Animatore crea(@RequestBody CreatePersonaleRequest request) {
        Animatore a = new Animatore();
        a.setUsername(request.getUsername());
        a.setEmail(request.getEmail());
        a.setPassword(request.getPassword());
        a.setNome(request.getNome());
        a.setCognome(request.getCognome());
        a.setTelefono(request.getTelefono());
        a.setRuolo(Ruolo.ANIMATORE);
        return repo.save(a);
    }

    @GetMapping
    public List<Animatore> lista() { return repo.findAll(); }

    @GetMapping("/{id}")
    public Animatore get(@PathVariable Long id) { return repo.findById(id).orElseThrow(); }
}
