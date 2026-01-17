package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.domain.TicketEvento;
import it.unicam.filiera.services.EventiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventi")
@Tag(name = "Eventi", description = "Gestione eventi + acquisto ticket (posti).")
public class EventiController {

    private final EventiService service;

    public EventiController(EventiService service) {
        this.service = service;
    }

    // ===== CRUD BASE (come già avevi in Swagger) =====

    @GetMapping
    @Operation(summary = "Lista eventi")
    public List<Evento> lista() {
        return service.lista();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Dettaglio evento")
    public Evento get(@PathVariable Long id) {
        return service.get(id);
    }

    @PostMapping
    @Operation(summary = "Crea evento")
    public Evento crea(@RequestBody Evento e) {
        return service.crea(e);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna evento")
    public Evento aggiorna(@PathVariable Long id, @RequestBody Evento e) {
        return service.aggiorna(id, e);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina evento")
    public void elimina(@PathVariable Long id) {
        service.elimina(id);
    }

    // ===== NUOVI ENDPOINT: TICKET =====

    public static class AcquistoTicketRequest {
        public Long acquirenteId;
        public int quantita;
    }

    @PostMapping("/{id}/ticket")
    @Operation(summary = "Acquista ticket (decrementa posti)")
    public TicketEvento acquistaTicket(@PathVariable Long id, @RequestBody AcquistoTicketRequest req) {
        return service.acquistaTicket(id, req.acquirenteId, req.quantita);
    }

    @GetMapping("/{id}/ticket")
    @Operation(summary = "Lista ticket per evento (demo)")
    public List<TicketEvento> listaTicket(@PathVariable Long id) {
        return service.listaTicketEvento(id);
    }
}
