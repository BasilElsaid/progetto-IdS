package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.EventoCreateDTO;
import it.unicam.filiera.evento.*;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.models.Animatore;
import it.unicam.filiera.repositories.EventiRepository;
import it.unicam.filiera.repositories.TicketEventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class EventiService {

    private final EventiRepository eventoRepo;

    public EventiService(EventiRepository eventoRepo) {
        this.eventoRepo = eventoRepo;
    }

    // CREATE
    public Evento creaEvento(EventoCreateDTO dto) {
        Evento evento = new Evento();
        evento.setNome(dto.getNome());
        evento.setDataOra(dto.getDataOra());
        evento.setPrezzo(dto.getPrezzo());
        evento.setTipo(dto.getTipo());
        evento.setPosti(dto.getPosti());
        return eventoRepo.save(evento);
    }

    // READ
    public List<Evento> getTuttiEventi() {
        return eventoRepo.findAll();
    }

    public Evento getEvento(Long id) {
        return eventoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));
    }

    // PATCH
    public Evento aggiornaEvento(Long id, EventoCreateDTO dto) {
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