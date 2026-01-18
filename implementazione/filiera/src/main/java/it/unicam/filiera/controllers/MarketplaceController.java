package it.unicam.filiera.controllers;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.services.ProdottiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
@PreAuthorize("hasAnyRole('ACQUIRENTE', 'GESTORE_PIATTAFORMA')")
public class MarketplaceController {

    private final ProdottiService prodottiService;

    public MarketplaceController(ProdottiService prodottiService) {
        this.prodottiService = prodottiService;
    }

    @GetMapping("/prodotti")
    public List<Prodotto> list(@RequestParam(required = false) String nome,
                              @RequestParam(required = false) String categoria) {
        return prodottiService.list(nome, categoria);
    }
}
