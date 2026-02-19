package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreatePersonaleRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.dto.update.UpdatePersonaleRequest;
import it.unicam.filiera.services.PersonaleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personale")
@Tag(name = "03 - Personale", description = "Gestione Curatore, Animatore e Gestore Piattaforma")
@PreAuthorize("hasAnyRole('CURATORE', 'ANIMATORE', 'GESTORE_PIATTAFORMA')")
public class PersonaleController {

    private final PersonaleService service;

    public PersonaleController(PersonaleService service) {
        this.service = service;
    }

    @GetMapping
    public List<UtenteResponse> lista() {
        return service.listaPersonale();
    }

    @DeleteMapping("/{id}")
    public void elimina(@PathVariable Long id) {
        service.eliminaPersonale(id);
    }

    @PatchMapping("/{id}")
    public UtenteResponse patch(@PathVariable Long id,
                                @RequestBody UpdatePersonaleRequest request) {
        return service.patchPersonale(id, request);
    }
}