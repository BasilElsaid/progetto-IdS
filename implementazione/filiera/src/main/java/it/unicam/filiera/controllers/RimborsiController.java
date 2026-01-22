package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.create.CreateRichiediRimborsoRequest;
import it.unicam.filiera.controllers.dto.create.CreateValutaRimborsoRequest;
import it.unicam.filiera.domain.RichiestaRimborso;
import it.unicam.filiera.services.RimborsiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rimborsi")
public class RimborsiController {

    private final RimborsiService service;

    public RimborsiController(RimborsiService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public RichiestaRimborso richiedi(@RequestBody CreateRichiediRimborsoRequest req) {
        return service.richiedi(req.getAcquirenteId(), req.getOrdineId(), req.getMotivazione());
    }

    @PatchMapping("/{rimborsoId}/valuta")
    @PreAuthorize("hasRole('GESTORE_PIATTAFORMA')")
    public RichiestaRimborso valuta(@PathVariable Long rimborsoId,
                                    @RequestBody CreateValutaRimborsoRequest req) {
        return service.valuta(rimborsoId, req.isApprova(), req.getNotaGestore());
    }

    @GetMapping("/acquirente/{acquirenteId}")
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public List<RichiestaRimborso> mie(@PathVariable Long acquirenteId) {
        return service.mie(acquirenteId);
    }

}
