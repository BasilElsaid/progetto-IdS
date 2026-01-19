package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.controllers.dto.response.UtenteResponse;
import it.unicam.filiera.services.PersonaleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale")
@Tag(name = "Personale", description = "Gestione Curatore, Animatore e Gestore Piattaforma")
public class PersonaleController {

    private final PersonaleService service;

    public PersonaleController(PersonaleService service) {
        this.service = service;
    }

    @PostMapping
    public UtenteResponse crea(@RequestBody CreatePersonaleRequest request) {
        return service.creaPersonale(request);
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'ANIMATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping
    public List<UtenteResponse> lista() {
        return service.listaPersonale();
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'ANIMATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/{id}")
    public UtenteResponse get(@PathVariable Long id) {
        return service.getPersonale(id);
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'ANIMATORE', 'GESTORE_PIATTAFORMA')")
    @DeleteMapping("/{id}")
    public void elimina(@PathVariable Long id) {
        service.eliminaPersonale(id);
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'ANIMATORE', 'GESTORE_PIATTAFORMA')")
    @PatchMapping("/{id}")
    public UtenteResponse patch(@PathVariable Long id, @RequestBody CreatePersonaleRequest request) {
        return service.patchPersonale(id, request);
    }
}