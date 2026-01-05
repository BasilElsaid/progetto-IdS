package it.unicam.filiera.controllers;

import it.unicam.filiera.models.Prodotto;
import it.unicam.filiera.services.ProdottiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
public class MarketplaceController {

    private final ProdottiService prodottiService;

    public MarketplaceController(ProdottiService prodottiService) {
        this.prodottiService = prodottiService;
    }

    @GetMapping("/prodotti")
    public List<Prodotto> list(@RequestParam(required = false) String nome,
                              @RequestParam(required = false) String origine) {
        return prodottiService.list(nome, origine);
    }
}
