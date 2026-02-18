package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.domain.TrasformazioneProdotto;
import it.unicam.filiera.dto.create.CreateTrasformazioneRequest;
import it.unicam.filiera.dto.response.TrasformazioneResponse;
import it.unicam.filiera.services.TrasformazioniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trasformazioni")
@Tag(name = "07 - Trasformazioni", description = "Gestione Processi di trasformazioni")
public class TrasformazioniController {

    private final TrasformazioniService service;

    public TrasformazioniController(TrasformazioniService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('TRASFORMATORE')")
    @PostMapping
    public List<TrasformazioneResponse> creaTrasformazioni(@RequestBody List<CreateTrasformazioneRequest> dtos) {
        return dtos.stream()
                .map(service::creaTrasformazione)
                .map(TrasformazioneResponse::from)
                .toList();
    }

    @GetMapping("/mie")
    @PreAuthorize("hasRole('TRASFORMATORE')")
    public List<TrasformazioneProdotto> mie() {
        return service.mieTrasformazioni();
    }

    @GetMapping("/trasformatore/{id}")
    @PreAuthorize("hasRole('GESTORE_PIATTAFORMA')")
    public List<TrasformazioneProdotto> perTrasformatore(@PathVariable Long id) {
        return service.listaPerTrasformatore(id);
    }
}
