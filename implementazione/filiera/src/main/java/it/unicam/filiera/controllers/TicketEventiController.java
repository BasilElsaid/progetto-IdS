package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateAcquistaTicketRequest;
import it.unicam.filiera.dto.response.TicketEventoResponse;
import it.unicam.filiera.services.TicketEventiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "14 - Ticket Eventi", description = "Gestione biglietti degli eventi")
@PreAuthorize("hasAnyRole('ACQUIRENTE', 'PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_TIPICITA', 'GESTORE_PIATTAFORMA')")
public class TicketEventiController {

    private final TicketEventiService ticketEventiService;

    public TicketEventiController(TicketEventiService ticketEventiService) {
        this.ticketEventiService = ticketEventiService;
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_TIPICITA')")
    @PostMapping("/eventi/{eventoId}/tickets")
    public List<TicketEventoResponse> acquistaTicket(@PathVariable Long eventoId,
                                                     @RequestBody(required = false) CreateAcquistaTicketRequest req) {
        int quantita = (req == null || req.quantita() == null) ? 1 : req.quantita();
        return ticketEventiService.acquistaTicket(eventoId, quantita);
    }

    @PreAuthorize("hasAnyRole('ACQUIRENTE', 'PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_TIPICITA')")
    @GetMapping("/tickets/miei")
    public List<TicketEventoResponse> listaMieiTicket() {
        return ticketEventiService.listaMieiTicket();
    }

    @GetMapping("/eventi/{eventoId}/tickets")
    @PreAuthorize("hasAnyRole('ANIMATORE', 'GESTORE_PIATTAFORMA')")
    public List<TicketEventoResponse> listaTicketEvento(@PathVariable Long eventoId) {
        return ticketEventiService.listaTicketEvento(eventoId);
    }

    @DeleteMapping("/tickets/{ticketId}")
    public void annullaTicket(@PathVariable Long ticketId) {
        ticketEventiService.annullaTicket(ticketId);
    }

    @PostMapping("/{numero}/checkin")
    public TicketEventoResponse checkin(@PathVariable int numero) {
        return ticketEventiService.checkIn(numero);
    }
}
