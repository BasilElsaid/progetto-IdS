package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateOffertaPacchettoRequest;
import it.unicam.filiera.controllers.dto.OffertaPacchettoResponse;
import it.unicam.filiera.controllers.dto.UpdateOffertaPacchettoStatoRequest;
import it.unicam.filiera.services.DistribuzioneService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
public class DistribuzioneController {

    private final DistribuzioneService service;

    public DistribuzioneController(DistribuzioneService service) {
        this.service = service;
    }

    @PostMapping("/offerte")
    public OffertaPacchettoResponse creaOfferta(@RequestBody CreateOffertaPacchettoRequest req) {
        return service.creaOfferta(req);
    }

    @GetMapping("/offerte")
    public List<OffertaPacchettoResponse> listaOfferte() {
        return service.listaOfferte();
    }

    @PatchMapping("/offerte/{offertaId}/stato")
    public OffertaPacchettoResponse aggiornaStatoStock(@PathVariable Long offertaId,
                                                       @RequestBody UpdateOffertaPacchettoStatoRequest req) {
        return service.aggiornaStatoStock(offertaId, req);
    }
}
