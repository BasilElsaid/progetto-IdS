package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CreateLottoRequest;
import it.unicam.filiera.controllers.dto.LottoResponse;
import it.unicam.filiera.controllers.dto.LottoTimelineResponse;
import it.unicam.filiera.services.LottoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lotti")
public class LottiController {

    private final LottoService lottoService;

    public LottiController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @PostMapping
    public LottoResponse crea(@RequestBody CreateLottoRequest req) {
        return lottoService.creaLotto(req);
    }

    @GetMapping("/{id}")
    public LottoResponse dettaglio(@PathVariable Long id) {
        return lottoService.getLotto(id);
    }

    @GetMapping("/{id}/timeline")
    public LottoTimelineResponse timeline(@PathVariable Long id) {
        return lottoService.timeline(id);
    }
}
