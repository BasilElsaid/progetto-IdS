package it.unicam.filiera.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.services.DistribuzioneService;
import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;

@RestController
@RequestMapping("/api")
public class DistribuzioneController {

    private final DistribuzioneService service;

    public DistribuzioneController(DistribuzioneService service) {
        this.service = service;
    }

    @PostMapping("/distributori/{id}/offerte")
    public OffertaPacchettoResponse creaOfferta(@PathVariable Long id,
                                                @RequestBody CreateOffertaPacchettoRequest req) {
        return service.creaOfferta(id, req);
    }

    @GetMapping("/distributori/{id}/offerte")
    public List<OffertaPacchettoResponse> listaOfferte(@PathVariable Long id) {
        return service.listaOffertePerDistributore(id);
    }

    @PatchMapping("/offerte/{offertaId}/stato")
    public OffertaPacchettoResponse aggiornaStato(@PathVariable Long offertaId,
                                                  @RequestBody UpdateOffertaPacchettoRequest req) {
        return service.aggiornaStatoOfferta(offertaId, req);
    }
}