package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.response.TicketEventoResponse;
import it.unicam.filiera.services.TicketEventiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@PreAuthorize("hasRole('ACQUIRENTE', 'PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_TIPICITA', 'ACQUIRENTE','GESTORE_PIATTAFORMA')")
public class TicketCheckinController {

    private final TicketEventiService ticketService;

    public TicketCheckinController(TicketEventiService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/{numero}/checkin")
    public TicketEventoResponse checkin(@PathVariable int numero) {
        return ticketService.checkIn(numero);
    }
}
