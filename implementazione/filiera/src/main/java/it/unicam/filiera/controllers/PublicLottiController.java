package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.LottoTimelineResponse;
import it.unicam.filiera.services.LottoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/lotto")
public class PublicLottiController {

    private final LottoService lottoService;

    public PublicLottiController(LottoService lottoService) {
        this.lottoService = lottoService;
    }

    @GetMapping("/{qrCode}")
    public LottoTimelineResponse get(@PathVariable String qrCode) {
        return lottoService.publicByQrCode(qrCode);
    }
}
