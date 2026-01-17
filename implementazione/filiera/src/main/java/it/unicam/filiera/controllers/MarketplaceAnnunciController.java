package it.unicam.filiera.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import it.unicam.filiera.services.MarketplaceService;
import it.unicam.filiera.controllers.dto.AnnuncioMarketplaceResponse;
import it.unicam.filiera.controllers.dto.CreateAnnuncioMarketplaceRequest;
import it.unicam.filiera.controllers.dto.UpdateAnnuncioMarketplaceRequest;

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
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Boolean attivo) {
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