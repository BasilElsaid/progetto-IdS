package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateEventoRequest;
import it.unicam.filiera.dto.response.EventoResponse;
import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.services.EventiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventi")
@Tag(name = "13 - Eventi", description = "Gestione eventi")
@PreAuthorize("hasAnyRole('ANIMATORE', 'GESTORE_PIATTAFORMA')")
public class EventiController {

	private final EventiService eventiService;

	public EventiController(EventiService eventiService) {
		this.eventiService = eventiService;
	}

	@PostMapping
	public Evento aggiungiEvento(@RequestBody CreateEventoRequest dto) {
		return eventiService.creaEvento(dto);
	}

	@PreAuthorize("hasAnyRole('ANIMATORE', 'GESTORE_PIATTAFORMA', 'ACQUIRENTE', 'PRODUTTORE', 'TRASFORMATORE', 'DISTRIBUTORE_TIPICITA')")
	@GetMapping
	public List<EventoResponse> getTuttiEventi() {
		return eventiService.getTuttiEventi();
	}

	@GetMapping("/{id}")
	public EventoResponse getEvento(@PathVariable Long id) {
		return eventiService.getEvento(id);
	}

	@PatchMapping("/{id}")
	public Evento aggiornaEvento(@PathVariable Long id,
								 @RequestBody CreateEventoRequest dto) {
		return eventiService.aggiornaEvento(id, dto);
	}

	@DeleteMapping("/{id}")
	public void eliminaEvento(@PathVariable Long id) {
		eventiService.eliminaEvento(id);
	}

}