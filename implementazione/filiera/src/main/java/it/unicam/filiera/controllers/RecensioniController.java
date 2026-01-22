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
        // Ora usiamo itemId invece di prodottoId
        return service.crea(req.getAcquirenteId(), req.getOrdineId(), req.getItemId(), req.isPacchetto(), req.getVoto(), req.getTesto());    }

    @GetMapping("/item/{itemId}")
    public List<Recensione> byItem(@PathVariable Long itemId) {
        return service.byItem(itemId);  // metodo nuovo nel service che cerca per itemId (prodotto o pacchetto)
    }

    @GetMapping("/acquirente/{acquirenteId}")
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public List<Recensione> byAcquirente(@PathVariable Long acquirenteId) {
        return service.byAcquirente(acquirenteId);
    }

    public static class CreaReq {
        private Long acquirenteId;
        private Long ordineId;
        private Long itemId;  // aggiornato da prodottoId a itemId
        private int voto;
        private String testo;
        private boolean pacchetto;

        public Long getAcquirenteId() { return acquirenteId; }
        public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

        public Long getOrdineId() { return ordineId; }
        public void setOrdineId(Long ordineId) { this.ordineId = ordineId; }

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }

        public int getVoto() { return voto; }
        public void setVoto(int voto) { this.voto = voto; }

        public String getTesto() { return testo; }
        public void setTesto(String testo) { this.testo = testo; }

        public boolean isPacchetto() { return pacchetto; }
        public void setPacchetto(boolean pacchetto) { this.pacchetto = pacchetto; }
    }
}