package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoStatoRequest;
import it.unicam.filiera.services.DistribuzioneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DistribuzioneController {

    private final DistribuzioneService service;

    public DistribuzioneController(DistribuzioneService service) {
        this.service = service;
    }

    @PostMapping("/api/distributori/{id}/offerte")
    public OffertaPacchettoResponse creaOfferta(@PathVariable Long id,
                                                @RequestBody CreateOffertaPacchettoRequest req) {
        return service.creaOfferta(id, req);
    }

    @GetMapping("/api/distributori/{id}/offerte")
    public List<OffertaPacchettoResponse> listaOfferte(@PathVariable Long id) {
        return service.listaOfferte(id);
    }

    @PatchMapping("/api/offerte/{offertaId}/stato")
    public OffertaPacchettoResponse aggiornaStato(@PathVariable Long offertaId,
                                                  @RequestBody UpdateOffertaPacchettoStatoRequest req) {
        return service.aggiornaStato(offertaId, req);
    }
}
