package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.controllers.dto.CreateTrasformazioneRequest;
import it.unicam.filiera.controllers.dto.TrasformazioneResponse;
import it.unicam.filiera.services.TrasformazioniService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/trasformazioni")
@Tag(name = "Trasformazioni", description = "Gestione trasformazioni prodotto (input → output) collegate a Processo e Trasformatore.")
public class TrasformazioniController {

    private final TrasformazioniService service;

    public TrasformazioniController(TrasformazioniService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crea una trasformazione (input → output)")
    public TrasformazioneResponse crea(@RequestBody CreateTrasformazioneRequest req) {
        return TrasformazioneResponse.from(
                service.creaTrasformazione(
                        req.processoId, req.trasformatoreId, req.inputId, req.outputId,
                        req.quantitaInput, req.quantitaOutput, req.note
                )
        );
    }

    @GetMapping("/processo/{processoId}")
    @Operation(summary = "Lista trasformazioni per processo")
    public List<TrasformazioneResponse> listaPerProcesso(@PathVariable Long processoId) {
        return service.listaPerProcesso(processoId).stream().map(TrasformazioneResponse::from).collect(toList());
    }

    @GetMapping("/trasformatore/{trasformatoreId}")
    @Operation(summary = "Lista trasformazioni per trasformatore")
    public List<TrasformazioneResponse> listaPerTrasformatore(@PathVariable Long trasformatoreId) {
        return service.listaPerTrasformatore(trasformatoreId).stream().map(TrasformazioneResponse::from).collect(toList());
    }
}
