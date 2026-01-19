package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.create.CreateEventoRequest;
import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.services.EventiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventi")
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

	@GetMapping
	public List<Evento> getTuttiEventi() {
		return eventiService.getTuttiEventi();
	}

	@GetMapping("/{id}")
	public Evento getEvento(@PathVariable Long id) {
		return eventiService.getEvento(id);
	}

	@PatchMapping("/{id}")
	public Evento aggiornaEvento(
			@PathVariable Long id,
			@RequestBody CreateEventoRequest dto
	) {
		return eventiService.aggiornaEvento(id, dto);
	}

	@DeleteMapping("/{id}")
	public void eliminaEvento(@PathVariable Long id) {
		eventiService.eliminaEvento(id);
	}
}