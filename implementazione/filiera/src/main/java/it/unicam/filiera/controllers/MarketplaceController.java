package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.dto.response.AnnuncioProdottoResponse;
import it.unicam.filiera.services.MarketplacePacchettiService;
import it.unicam.filiera.services.MarketplaceProdottiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
@Tag(name = "10 - Marketplace", description = "Visualizzazione degli annunci")
public class MarketplaceController {

    private final MarketplaceProdottiService marketplaceProdottiService;
    private final MarketplacePacchettiService marketplacePacchettiService;

    public MarketplaceController(MarketplaceProdottiService marketplaceProdottiService, MarketplacePacchettiService marketplacePacchettiService) {
        this.marketplaceProdottiService = marketplaceProdottiService;
        this.marketplacePacchettiService = marketplacePacchettiService;
    }

    @GetMapping("/prodotti")
    public List<AnnuncioProdottoResponse> listaProdotti() {
        return marketplaceProdottiService.listaTuttiProdottiResponse();
    }

    @GetMapping("/pacchetti")
    public List<AnnuncioPacchettoResponse> listaPacchetti() {
        return marketplacePacchettiService.listaTuttiProdottiResponse();
    }
}
