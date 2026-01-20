package it.unicam.filiera.controllers;

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
    public RichiestaRimborso richiedi(@RequestBody RichiediReq req) {
        return service.richiedi(req.getAcquirenteId(), req.getOrdineId(), req.getMotivazione());
    }

    @GetMapping("/acquirente/{acquirenteId}")
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public List<RichiestaRimborso> mie(@PathVariable Long acquirenteId) {
        return service.mie(acquirenteId);
    }

    @PatchMapping("/{rimborsoId}/valuta")
    @PreAuthorize("hasRole('GESTORE_PIATTAFORMA')")
    public RichiestaRimborso valuta(@PathVariable Long rimborsoId, @RequestBody ValutaReq req) {
        return service.valuta(rimborsoId, req.isApprova(), req.getNotaGestore());
    }

    public static class RichiediReq {
        private Long acquirenteId;
        private Long ordineId;
        private String motivazione;

        public Long getAcquirenteId() { return acquirenteId; }
        public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

        public Long getOrdineId() { return ordineId; }
        public void setOrdineId(Long ordineId) { this.ordineId = ordineId; }

        public String getMotivazione() { return motivazione; }
        public void setMotivazione(String motivazione) { this.motivazione = motivazione; }
    }

    public static class ValutaReq {
        private boolean approva;
        private String notaGestore;

        public boolean isApprova() { return approva; }
        public void setApprova(boolean approva) { this.approva = approva; }

        public String getNotaGestore() { return notaGestore; }
        public void setNotaGestore(String notaGestore) { this.notaGestore = notaGestore; }
    }
}
