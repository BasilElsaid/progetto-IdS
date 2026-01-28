package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateAcquirenteRequest;
import it.unicam.filiera.dto.response.PacchettoResponse;
import it.unicam.filiera.dto.response.ProdottoResponse;
import it.unicam.filiera.dto.response.UtenteResponse;
import it.unicam.filiera.dto.update.UpdateAcquirenteRequest;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.services.AcquirentiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acquirenti")
@Tag(name = "04 - Acquirenti", description = "Gestione Specifica di Acquirenti")
public class AcquirentiController {

    private final AcquirentiService service;

    public AcquirentiController(AcquirentiService service) {
        this.service = service;
    }

    @PostMapping
    public UtenteResponse crea(@RequestBody CreateAcquirenteRequest request) {
        return service.creaAcquirente(request);
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
    @GetMapping
    public List<UtenteResponse> lista() {
        return service.listaAcquirenti();
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/{id}")
    public UtenteResponse get(@PathVariable Long id) {
        return service.getAcquirente(id);
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAcquirente(id);
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
    @PatchMapping("/{id}")
    public UtenteResponse patch(@PathVariable Long id,
                                @RequestBody UpdateAcquirenteRequest request) {
        return service.patchAcquirente(id, request);
    }

    @GetMapping("/{acquirenteId}/prodotti")
    public List<ProdottoResponse> getProdottiAcquistati(@PathVariable Long acquirenteId) {
        return service.getProdottiAcquistati(acquirenteId);
    }

    @GetMapping("/{acquirenteId}/pacchetti")
    public List<PacchettoResponse> getPacchettiAcquistati(@PathVariable Long acquirenteId) {
        return service.getPacchettiAcquistati(acquirenteId);
    }
}