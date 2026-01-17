package it.unicam.filiera.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.services.TrasformazioniService;
import it.unicam.filiera.controllers.dto.CreateTrasformazioneRequest;
import it.unicam.filiera.controllers.dto.TrasformazioneResponse;

@RestController
@RequestMapping("/api/trasformazioni")
public class TrasformazioniController {

    private final TrasformazioniService service;

    public TrasformazioniController(TrasformazioniService service) {
        this.service = service;
    }

    @PostMapping
    public TrasformazioneResponse crea(@RequestBody CreateTrasformazioneRequest req) {
        return TrasformazioneResponse.from(
                service.creaTrasformazione(
                        req.processoId, req.trasformatoreId, req.inputId, req.outputId,
                        req.quantitaInput, req.quantitaOutput, req.note
                )
        );
    }

    @GetMapping("/processo/{processoId}")
    public List<TrasformazioneResponse> listaPerProcesso(@PathVariable Long processoId) {
        return service.listaPerProcesso(processoId).stream()
                .map(TrasformazioneResponse::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/trasformatore/{trasformatoreId}")
    public List<TrasformazioneResponse> listaPerTrasformatore(@PathVariable Long trasformatoreId) {
        return service.listaPerTrasformatore(trasformatoreId).stream()
                .map(TrasformazioneResponse::from)
                .collect(Collectors.toList());
    }
}