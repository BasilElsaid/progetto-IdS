package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.AcquistaTicketRequest;
import it.unicam.filiera.controllers.dto.TicketEventoResponse;
import it.unicam.filiera.services.TicketEventiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketEventiController {

    private final TicketEventiService ticketEventiService;

    public TicketEventiController(TicketEventiService ticketEventiService) {
        this.ticketEventiService = ticketEventiService;
    }

    /**
     * Acquista uno o piu ticket per un evento.
     * Ruolo richiesto: ACQUIRENTE
     */
    @PostMapping("/eventi/{eventoId}/tickets")
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public List<TicketEventoResponse> acquistaTicket(
            @PathVariable Long eventoId,
            @RequestBody(required = false) AcquistaTicketRequest req
    ) {
        int quantita = (req == null || req.getQuantita() == null) ? 1 : req.getQuantita();
        return ticketEventiService.acquistaTicket(eventoId, quantita);
    }

    /**
     * Lista i ticket del cliente loggato.
     * Ruolo richiesto: ACQUIRENTE
     */
    @GetMapping("/tickets/miei")
    @PreAuthorize("hasRole('ACQUIRENTE')")
    public List<TicketEventoResponse> listaMieiTicket() {
        return ticketEventiService.listaMieiTicket();
    }

    /**
     * Lista tutti i ticket di un evento (per controllo/organizzazione).
     * Ruoli richiesti: ANIMATORE o GESTORE_PIATTAFORMA
     */
    @GetMapping("/eventi/{eventoId}/tickets")
    @PreAuthorize("hasAnyRole('ANIMATORE','GESTORE_PIATTAFORMA')")
    public List<TicketEventoResponse> listaTicketEvento(@PathVariable Long eventoId) {
        return ticketEventiService.listaTicketEvento(eventoId);
    }

    /**
     * Convalida un ticket (marcatura come "usato").
     * Ruoli richiesti: ANIMATORE o GESTORE_PIATTAFORMA
     */
    @PatchMapping("/tickets/{numeroTicket}/usa")
    @PreAuthorize("hasAnyRole('ANIMATORE','GESTORE_PIATTAFORMA')")
    public TicketEventoResponse usaTicket(@PathVariable int numeroTicket) {
        return ticketEventiService.usaTicket(numeroTicket);
    }

    /**
     * Annulla un ticket (solo proprietario ACQUIRENTE o Gestore Piattaforma).
     */
    @DeleteMapping("/tickets/{ticketId}")
    @PreAuthorize("hasAnyRole('ACQUIRENTE','GESTORE_PIATTAFORMA')")
    public void annullaTicket(@PathVariable Long ticketId) {
        ticketEventiService.annullaTicket(ticketId);
    }
}
