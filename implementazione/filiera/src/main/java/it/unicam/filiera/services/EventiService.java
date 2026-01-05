package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.EventoCreateDTO;
import it.unicam.filiera.evento.*;
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

    public Evento creaEvento(EventoCreateDTO dto) {
        Evento evento = new Evento();
        evento.setNome(dto.nome);
        evento.setDataOra(dto.dataOra);
        evento.setPrezzo(dto.prezzo);
        evento.setTipo(dto.tipo);
        evento.setPosti(dto.posti);
        evento.setAnimatore(null); // ok, Hibernate accetta null
        return eventoRepo.save(evento);
    }

    public List<Evento> getTuttiEventi() {
        return eventoRepo.findAll();
    }

    public Optional<Evento> getEvento(Long id) {
        return eventoRepo.findById(id);
    }

    public void eliminaEvento(Long id) {
        eventoRepo.deleteById(id);
    }

    public Evento aggiornaEvento(Long id, Evento eventoAggiornato) {
        Evento e = eventoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento non trovato: " + id));
        e.setNome(eventoAggiornato.getNome());
        e.setDataOra(eventoAggiornato.getDataOra());
        e.setPrezzo(eventoAggiornato.getPrezzo());
        e.setTipo(eventoAggiornato.getTipo());
        e.setPosti(eventoAggiornato.getPosti());
        e.setAnimatore(null);
        return e;
    }
}