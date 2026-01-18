package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateTrasformazioneRequest;
import it.unicam.filiera.controllers.dto.TrasformazioneResponse;
import it.unicam.filiera.services.TrasformazioniService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
