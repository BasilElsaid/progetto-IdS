package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateTrasformazioneRequest;
import it.unicam.filiera.dto.response.TrasformazioneResponse;
import it.unicam.filiera.services.TrasformazioniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trasformazioni")
@Tag(name = "07 - Trasformazioni", description = "Gestione Processi di trasformazioni")
@PreAuthorize("hasAnyRole('TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
public class TrasformazioniController {

    private final TrasformazioniService service;

    public TrasformazioniController(TrasformazioniService service) {
        this.service = service;
    }

    @PostMapping
    public TrasformazioneResponse crea(@RequestBody CreateTrasformazioneRequest req) {
        return TrasformazioneResponse.from(
                service.creaTrasformazione(
                        req.getProcessoId(),
                        req.getTrasformatoreId(),
                        req.getInputId(),
                        req.getOutputId(),
                        req.getQuantitaInput(),
                        req.getQuantitaOutput(),
                        req.getNote()
                )
        );
    }

    @GetMapping("/processo/{processoId}")
    public List<TrasformazioneResponse> listaPerProcesso(@PathVariable Long processoId) {
        return service.listaPerProcesso(processoId).stream().map(TrasformazioneResponse::from).toList();
    }

    @GetMapping("/trasformatore/{trasformatoreId}")
    public List<TrasformazioneResponse> listaPerTrasformatore(@PathVariable Long trasformatoreId) {
        return service.listaPerTrasformatore(trasformatoreId).stream().map(TrasformazioneResponse::from).toList();
    }
}
