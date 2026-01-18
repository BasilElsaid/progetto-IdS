package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.*;
import it.unicam.filiera.enums.CategoriaProdotto;
import it.unicam.filiera.services.MarketplaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace/annunci")
public class MarketplaceAnnunciController {

    private final MarketplaceService marketplaceService;

    public MarketplaceAnnunciController(MarketplaceService marketplaceService) {
        this.marketplaceService = marketplaceService;
    }

    @PostMapping
    public AnnuncioMarketplaceResponse creaAnnuncio(@RequestBody CreateAnnuncioMarketplaceRequest req) {
        return marketplaceService.creaAnnuncio(req);
    }

    @GetMapping
    public List<AnnuncioMarketplaceResponse> listaAnnunci(
            @RequestParam(required = false) Long aziendaId,
            @RequestParam(required = false) CategoriaProdotto categoria,
            @RequestParam(required = false) Boolean attivo
    ) {
        return marketplaceService.listaAnnunci(aziendaId, categoria, attivo);
    }

    @GetMapping("/{id}")
    public AnnuncioMarketplaceResponse dettaglio(@PathVariable Long id) {
        return marketplaceService.getAnnuncio(id);
    }

    @PatchMapping("/{id}")
    public AnnuncioMarketplaceResponse aggiorna(@PathVariable Long id,
                                                @RequestBody UpdateAnnuncioMarketplaceRequest req) {
        return marketplaceService.aggiornaAnnuncio(id, req);
    }
}
