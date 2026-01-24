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
        evento.setNome(dto.nome());
        evento.setDataOra(dto.dataOra());
        evento.setPrezzo(dto.prezzo());
        evento.setTipo(dto.tipo());
        evento.setPosti(dto.posti());
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

        if (dto.nome() != null) e.setNome(dto.nome());
        if (dto.dataOra() != null) e.setDataOra(dto.dataOra());
        if (dto.prezzo() != null) e.setPrezzo(dto.prezzo());
        if (dto.tipo() != null) e.setTipo(dto.tipo());
        if (dto.posti() != null) e.setPosti(dto.posti());

        return e; // JPA dirty checking
    }

    // DELETE
    public void eliminaEvento(Long id) {
        Evento e = eventoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));
        eventoRepo.delete(e);
    }
}