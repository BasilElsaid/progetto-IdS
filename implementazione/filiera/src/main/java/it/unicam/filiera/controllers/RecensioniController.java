package it.unicam.filiera.controllers;

import it.unicam.filiera.domain.Recensione;
import it.unicam.filiera.services.RecensioniService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recensioni")
public class RecensioniController {

    private final RecensioniService service;

    public RecensioniController(RecensioniService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public Recensione crea(@RequestBody CreaReq req) {
        return service.crea(req.getAcquirenteId(), req.getOrdineId(), req.getProdottoId(), req.getVoto(), req.getTesto());
    }

    @GetMapping("/prodotto/{prodottoId}")
    public List<Recensione> byProdotto(@PathVariable Long prodottoId) {
        return service.byProdotto(prodottoId);
    }

    @GetMapping("/acquirente/{acquirenteId}")
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public List<Recensione> byAcquirente(@PathVariable Long acquirenteId) {
        return service.byAcquirente(acquirenteId);
    }

    public static class CreaReq {
        private Long acquirenteId;
        private Long ordineId;
        private Long prodottoId;
        private int voto;
        private String testo;

        public Long getAcquirenteId() { return acquirenteId; }
        public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

        public Long getOrdineId() { return ordineId; }
        public void setOrdineId(Long ordineId) { this.ordineId = ordineId; }

        public Long getProdottoId() { return prodottoId; }
        public void setProdottoId(Long prodottoId) { this.prodottoId = prodottoId; }

        public int getVoto() { return voto; }
        public void setVoto(int voto) { this.voto = voto; }

        public String getTesto() { return testo; }
        public void setTesto(String testo) { this.testo = testo; }
    }
}
