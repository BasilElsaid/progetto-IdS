package it.unicam.filiera.services;

import it.unicam.filiera.dto.create.CreateEventoRequest;
import it.unicam.filiera.dto.response.EventoResponse;
import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.repositories.EventiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventiService {

    private final EventiRepository eventoRepo;

    public EventiService(EventiRepository eventoRepo) {
        this.eventoRepo = eventoRepo;
    }

    // CREATE
    public Evento creaEvento(CreateEventoRequest dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDataOra(dto.getDataOra());
        evento.setPrezzo(dto.getPrezzo());
        evento.setTipo(dto.getTipo());
        evento.setPosti(dto.getPosti());
        return eventoRepo.save(evento);
    }

    // READ
    public List<EventoResponse> getTuttiEventi() {
        return eventoRepo.findAll().stream()
                .map(e -> new EventoResponse(
                        e.getId(),
                        e.getNome(),
                        e.getDataOra(),
                        e.getPrezzo(),
                        e.getTipo(),
                        e.getPosti()
                ))
                .toList();
    }

    public EventoResponse getEvento(Long id) {
        Evento e = eventoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        return new EventoResponse(
                e.getId(),
                e.getNome(),
                e.getDataOra(),
                e.getPrezzo(),
                e.getTipo(),
                e.getPosti()
        );
    }

    // PATCH
    public Evento aggiornaEvento(Long id, CreateEventoRequest dto) {
        Evento e = eventoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato:"));

        if (dto.getNome() != null) e.setNome(dto.getNome());
        if (dto.getDataOra() != null) e.setDataOra(dto.getDataOra());
        if (dto.getPrezzo() != null) e.setPrezzo(dto.getPrezzo());
        if (dto.getTipo() != null) e.setTipo(dto.getTipo());
        if (dto.getPosti() != null) e.setPosti(dto.getPosti());

        return e; // JPA dirty checking
    }

    // DELETE
    public void eliminaEvento(Long id) {
        Evento e = eventoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));
        eventoRepo.delete(e);
    }
}