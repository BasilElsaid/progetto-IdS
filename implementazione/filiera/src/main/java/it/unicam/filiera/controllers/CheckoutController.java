package it.unicam.filiera.controllers;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.services.CheckoutService;
import it.unicam.filiera.controllers.dto.CheckoutRequest;
import it.unicam.filiera.controllers.dto.PagaOrdineRequest;

import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.domain.Ordine;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping
    public Ordine creaOrdine(@RequestBody CheckoutRequest req) {
        return service.creaOrdineDaAnnuncio(req);
    }

    @PostMapping("/{ordineId}/paga")
    public PagamentoOrdine paga(@PathVariable Long ordineId, @RequestBody PagaOrdineRequest req) {
        return service.pagaOrdine(ordineId, req);
    }
}