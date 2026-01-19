package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.create.CreatePacchettoRequest;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.services.PacchettiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacchetti")
@PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
public class PacchettiController {

    private final PacchettiService service;

    public PacchettiController(PacchettiService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pacchetto> lista() {
        return service.lista();
    }

    @PostMapping
    public Pacchetto crea(@RequestBody CreatePacchettoRequest req) {
        return service.crea(req);
    }

    @GetMapping("/{id}")
    public Pacchetto get(@PathVariable Long id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public void elimina(@PathVariable Long id) {
        service.elimina(id);
    }
}
