package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.*;
import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.domain.PagamentoOrdine;
import it.unicam.filiera.services.CheckoutService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
public class CheckoutController {

    private final CheckoutService service;

    public CheckoutController(CheckoutService service) {
        this.service = service;
    }

    @PostMapping
    public CheckoutResponse checkout(@RequestBody CheckoutRequest req) {
        Ordine ordine = service.creaOrdineDaAnnuncio(req);
        return CheckoutResponse.from(ordine);
    }

    @PostMapping("/{ordineId}/paga")
    public PagamentoResponse paga(@PathVariable Long ordineId,
                                  @RequestBody PagaOrdineRequest req) {
        PagamentoOrdine p = service.pagaOrdine(ordineId, req.getMetodo());
        return PagamentoResponse.from(p);
    }
}
