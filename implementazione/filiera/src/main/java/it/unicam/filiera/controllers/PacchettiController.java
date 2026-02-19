package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreatePacchettoRequest;
import it.unicam.filiera.domain.Pacchetto;
import it.unicam.filiera.dto.response.PacchettoResponse;
import it.unicam.filiera.dto.update.UpdatePacchettoRequest;
import it.unicam.filiera.services.PacchettiService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacchetti")
@Tag(name = "06 - Pacchetti", description = "Gestione pacchetti")
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

    @PreAuthorize("hasRole('DISTRIBUTORE_TIPICITA')")
    @PostMapping
    public List<PacchettoResponse> creaBatch(@RequestBody @Valid List<CreatePacchettoRequest> dtos) {
        return dtos.stream()
                .map(service::crea)
                .toList();
    }

    @DeleteMapping("/{id}")
    public void elimina(@PathVariable Long id) {
        service.elimina(id);
    }

}
