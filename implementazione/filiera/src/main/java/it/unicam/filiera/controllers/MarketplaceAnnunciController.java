package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateAnnuncioProdottoRequest;
import it.unicam.filiera.dto.create.CreateAnnuncioPacchettoRequest;
import it.unicam.filiera.dto.response.AnnuncioProdottoResponse;
import it.unicam.filiera.dto.response.AnnuncioPacchettoResponse;
import it.unicam.filiera.dto.update.UpdateAnnuncioMarketplaceRequest;
import it.unicam.filiera.enums.CategoriaProdotto;
import it.unicam.filiera.services.MarketplacePacchettiService;
import it.unicam.filiera.services.MarketplaceProdottiService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/marketplace/annunci")
@Tag(name = "09 - Marketplace Annunci", description = "Gestione annunci del marketplace")
public class MarketplaceAnnunciController {

    private final MarketplaceProdottiService marketplaceProdottiService;
    private final MarketplacePacchettiService marketplacePacchettiService;

    public MarketplaceAnnunciController(MarketplaceProdottiService marketplaceProdottiService, MarketplacePacchettiService marketplacePacchettiService) {
        this.marketplaceProdottiService = marketplaceProdottiService;
        this.marketplacePacchettiService = marketplacePacchettiService;
    }

    // PRODOTTI
    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE')")
    @PostMapping("/prodotti")
    public List<AnnuncioProdottoResponse> creaProdotti(
            @RequestBody List<CreateAnnuncioProdottoRequest> dtos) {
        return marketplaceProdottiService.creaAnnunciBatch(dtos);
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/prodotti")
    public List<AnnuncioProdottoResponse> listaProdotti(@RequestParam Optional<Long> aziendaId,
                                                        @RequestParam Optional<CategoriaProdotto> categoria,
                                                        @RequestParam Optional<Boolean> attivo) {
        String categoriaStr = categoria.map(Enum::name).orElse(null);
        return marketplaceProdottiService.listaAnnunci(aziendaId.orElse(null), categoriaStr, attivo.orElse(null));
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/prodotti/{id}")
    public AnnuncioProdottoResponse dettaglioProdotto(@PathVariable Long id) {
        return marketplaceProdottiService.getAnnuncio(id);
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE')")
    @PatchMapping("/prodotti/{id}")
    public AnnuncioProdottoResponse aggiornaProdotto(@PathVariable Long id,
                                                     @RequestBody UpdateAnnuncioMarketplaceRequest req) {
        return marketplaceProdottiService.aggiornaAnnuncio(id, req);
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
    @DeleteMapping("/prodotti/{id}")
    public void eliminaAnnuncioProdotto(@PathVariable Long id) {
        marketplaceProdottiService.eliminaAnnuncio(id);
    }

    // PACCHETTI
    @PreAuthorize("hasRole('DISTRIBUTORE_TIPICITA')")
    @PostMapping("/pacchetti")
    public List<AnnuncioPacchettoResponse> creaPacchetti(
            @RequestBody List<CreateAnnuncioPacchettoRequest> dtos) {
        return marketplacePacchettiService.creaAnnunciBatch(dtos);
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/pacchetti")
    public List<AnnuncioPacchettoResponse> listaPacchetti(
            @RequestParam Optional<Long> aziendaId,
            @RequestParam Optional<Boolean> attivo
    ) {
        return marketplacePacchettiService.listaAnnunci(aziendaId.orElse(null), attivo.orElse(null));
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
    @GetMapping("/pacchetti/{id}")
    public AnnuncioPacchettoResponse dettaglioPacchetto(@PathVariable Long id) {
        return marketplacePacchettiService.getAnnuncio(id);
    }

    @PreAuthorize("hasRole('DISTRIBUTORE_TIPICITA')")
    @PatchMapping("/pacchetti/{id}")
    public AnnuncioPacchettoResponse aggiornaPacchetto(@PathVariable Long id,
                                                       @RequestBody UpdateAnnuncioMarketplaceRequest req) {
        return marketplacePacchettiService.aggiornaAnnuncio(id, req);
    }

    @PreAuthorize("hasAnyRole('DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
    @DeleteMapping("/pacchetti/{id}")
    public void eliminaAnnuncioPacchetto(@PathVariable Long id) {
        marketplacePacchettiService.eliminaAnnuncioPacchetto(id);
    }

}
