package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.TicketEventoResponse;
import it.unicam.filiera.services.TicketEventiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
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
