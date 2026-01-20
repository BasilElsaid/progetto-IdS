package it.unicam.filiera.controllers;

import it.unicam.filiera.domain.Carrello;
import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.enums.MetodoPagamento;
import it.unicam.filiera.services.CarrelloService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrello")
@PreAuthorize("hasRole('ACQUIRENTE')")
public class CarrelloController {

    private final CarrelloService service;

    public CarrelloController(CarrelloService service) {
        this.service = service;
    }

    @GetMapping("/{acquirenteId}")
    public Carrello get(@PathVariable Long acquirenteId) {
        return service.getOrCreate(acquirenteId);
    }

    @PostMapping("/{acquirenteId}/items")
    public Carrello add(@PathVariable Long acquirenteId, @RequestBody AddItemReq req) {
        return service.aggiungiItem(acquirenteId, req.getAnnuncioId(), req.getQuantita());
    }

    @DeleteMapping("/{acquirenteId}/items/{itemId}")
    public Carrello remove(@PathVariable Long acquirenteId, @PathVariable Long itemId) {
        return service.rimuoviItem(acquirenteId, itemId);
    }

    // CHECKOUT: crea ordine IN_ATTESA_PAGAMENTO e svuota carrello
    @PostMapping("/{acquirenteId}/checkout")
    public Ordine checkout(@PathVariable Long acquirenteId) {
        return service.checkout(acquirenteId);
    }

    // PAGA: cambia ordine -> PAGATO (senza CheckoutController)
    @PostMapping("/{acquirenteId}/paga")
    public PagamentoOrdine paga(@PathVariable Long acquirenteId, @RequestBody PagaReq req) {
        return service.pagaOrdine(acquirenteId, req.getOrdineId(), req.getMetodo());
    }

    public static class AddItemReq {
        private Long annuncioId;
        private int quantita;

        public Long getAnnuncioId() { return annuncioId; }
        public void setAnnuncioId(Long annuncioId) { this.annuncioId = annuncioId; }

        public int getQuantita() { return quantita; }
        public void setQuantita(int quantita) { this.quantita = quantita; }
    }

    public static class PagaReq {
        private Long ordineId;
        private MetodoPagamento metodo;

        public Long getOrdineId() { return ordineId; }
        public void setOrdineId(Long ordineId) { this.ordineId = ordineId; }

        public MetodoPagamento getMetodo() { return metodo; }
        public void setMetodo(MetodoPagamento metodo) { this.metodo = metodo; }
    }
}
