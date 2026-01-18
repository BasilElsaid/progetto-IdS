package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoStatoRequest;
import it.unicam.filiera.services.DistribuzioneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DistribuzioneController {

    private final DistribuzioneService service;

    public DistribuzioneController(DistribuzioneService service) {
        this.service = service;
    }

    // POST /api/distributori/{id}/offerte
    @PostMapping("/distributori/{id}/offerte")
    public OffertaPacchettoResponse creaOfferta(@PathVariable("id") Long distributoreId,
                                                @RequestBody CreateOffertaPacchettoRequest req) {
        return service.creaOfferta(distributoreId, req);
    }

    // GET /api/distributori/{id}/offerte
    @GetMapping("/distributori/{id}/offerte")
    public List<OffertaPacchettoResponse> listaOfferte(@PathVariable("id") Long distributoreId) {
        return service.listaOfferte(distributoreId);
    }

    // PATCH /api/offerte/{offertaId}/stato
    @PatchMapping("/offerte/{offertaId}/stato")
    public OffertaPacchettoResponse aggiornaStatoStock(@PathVariable Long offertaId,
                                                       @RequestBody UpdateOffertaPacchettoStatoRequest req) {
        return service.aggiornaStatoStock(offertaId, req);
    }
}
