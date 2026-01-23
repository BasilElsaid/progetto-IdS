package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateAziendaRequest;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.dto.update.UpdateAziendaRequest;
import it.unicam.filiera.services.AziendeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aziende")
@Tag(name = "02 - Aziende", description = "Gestione Produttori, Trasformatori e Distributori di tipicità")
public class AziendeController {

    private final AziendeService service;

    public AziendeController(AziendeService service) {
        this.service = service;
    }

    @PostMapping
    public UtenteResponse crea(@Valid @RequestBody CreateAziendaRequest request) {
        // switch interno per decidere il tipo di azienda
        return service.creaAzienda(request);
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping
    public List<UtenteResponse> listaTutti() {
        return service.listaAziende();
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/{id}")
    public UtenteResponse get(@PathVariable Long id) {
        return service.getAzienda(id);
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAzienda(id);
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @PatchMapping("/{id}")
    public UtenteResponse patch(@PathVariable Long id, @RequestBody UpdateAziendaRequest request) {
        return service.patchAzienda(id, request);
    }
}