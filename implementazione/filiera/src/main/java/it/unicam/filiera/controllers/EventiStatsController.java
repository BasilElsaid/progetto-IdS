package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.response.EventoStatsResponse;
import it.unicam.filiera.services.EventiStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventi")
@Tag(name = "15 - Stats Eventi", description = "Gestione stats degli eventi")
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
