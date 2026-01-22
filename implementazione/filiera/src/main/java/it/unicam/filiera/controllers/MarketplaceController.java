package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.controllers.dto.response.AnnuncioProdottoResponse;
import it.unicam.filiera.controllers.dto.response.ProdottoResponse;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.services.MarketplacePacchettiService;
import it.unicam.filiera.services.MarketplaceProdottiService;
import it.unicam.filiera.services.ProdottiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marketplace")
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
