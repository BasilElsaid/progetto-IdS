package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.create.CreateAnnuncioProdottoRequest;
import it.unicam.filiera.controllers.dto.create.CreateAnnuncioPacchettoRequest;
import it.unicam.filiera.controllers.dto.response.AnnuncioProdottoResponse;
import it.unicam.filiera.controllers.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.controllers.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.enums.CategoriaProdotto;
import it.unicam.filiera.services.MarketplacePacchettiService;
import it.unicam.filiera.services.MarketplaceProdottiService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/marketplace/annunci")
@PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
public class MarketplaceAnnunciController {

    private final MarketplaceProdottiService marketplaceProdottiService;
    private final MarketplacePacchettiService marketplacePacchettiService;

    public MarketplaceAnnunciController(MarketplaceProdottiService marketplaceProdottiService, MarketplacePacchettiService marketplacePacchettiService) {
        this.marketplaceProdottiService = marketplaceProdottiService;
        this.marketplacePacchettiService = marketplacePacchettiService;
    }

    // -------------------- PRODOTTI --------------------

    @PostMapping("/prodotti")
    public AnnuncioProdottoResponse creaProdotto(@RequestBody CreateAnnuncioProdottoRequest req) {
        return marketplaceProdottiService.creaAnnuncio(req);
    }

    @GetMapping("/prodotti")
    public List<AnnuncioProdottoResponse> listaProdotti(
            @RequestParam Optional<Long> aziendaId,
            @RequestParam Optional<CategoriaProdotto> categoria,
            @RequestParam Optional<Boolean> attivo
    ) {
        String categoriaStr = categoria.map(Enum::name).orElse(null);
        return marketplaceProdottiService.listaAnnunci(aziendaId.orElse(null), categoriaStr, attivo.orElse(null));
    }

    @GetMapping("/prodotti/{id}")
    public AnnuncioProdottoResponse dettaglioProdotto(@PathVariable Long id) {
        return marketplaceProdottiService.getAnnuncio(id);
    }

    @PatchMapping("/prodotti/{id}")
    public AnnuncioProdottoResponse aggiornaProdotto(@PathVariable Long id,
                                                     @RequestBody UpdateAnnuncioMarketplaceRequest req) {
        return marketplaceProdottiService.aggiornaAnnuncio(id, req);
    }

    @DeleteMapping("/prodotti/{id}")
    public void eliminaAnnuncioProdotto(@PathVariable Long id) {
        marketplaceProdottiService.eliminaAnnuncio(id);
    }

    // -------------------- PACCHETTI --------------------

    @PostMapping("/pacchetti")
    public AnnuncioPacchettoResponse creaPacchetto(@RequestBody CreateAnnuncioPacchettoRequest req) {
        return marketplacePacchettiService.creaAnnuncioPacchetto(req);
    }

    @GetMapping("/pacchetti")
    public List<AnnuncioPacchettoResponse> listaPacchetti(
            @RequestParam Optional<Long> aziendaId,
            @RequestParam Optional<Boolean> attivo
    ) {
        return marketplacePacchettiService.listaAnnunci(aziendaId.orElse(null), attivo.orElse(null));
    }

    @GetMapping("/pacchetti/{id}")
    public AnnuncioPacchettoResponse dettaglioPacchetto(@PathVariable Long id) {
        return marketplacePacchettiService.getAnnuncio(id);
    }

    @PatchMapping("/pacchetti/{id}")
    public AnnuncioPacchettoResponse aggiornaPacchetto(@PathVariable Long id,
                                                       @RequestBody UpdateAnnuncioMarketplaceRequest req) {
        return marketplacePacchettiService.aggiornaAnnuncio(id, req);
    }

    @DeleteMapping("/pacchetti/{id}")
    public void eliminaAnnuncioPacchetto(@PathVariable Long id) {
        marketplacePacchettiService.eliminaAnnuncioPacchetto(id);
    }

}
