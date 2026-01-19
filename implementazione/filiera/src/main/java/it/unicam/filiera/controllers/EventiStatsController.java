package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.response.EventoStatsResponse;
import it.unicam.filiera.services.EventiStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventi")
public class EventiStatsController {

    private final EventiStatsService statsService;

    public EventiStatsController(EventiStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/{id}/stats")
    public EventoStatsResponse stats(@PathVariable Long id) {
        return statsService.stats(id);
    }
}
