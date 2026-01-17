package it.unicam.filiera.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.domain.TicketEvento;
import it.unicam.filiera.models.Acquirente;

import it.unicam.filiera.repositories.EventiRepository;
import it.unicam.filiera.repositories.TicketEventoRepository;
import it.unicam.filiera.repositories.AcquirenteRepository;

@Service
public class EventiService {

    private final EventiRepository eventiRepository;
    private final TicketEventoRepository ticketRepository;
    private final AcquirenteRepository acquirenteRepository;

    public EventiService(EventiRepository eventiRepository,
                         TicketEventoRepository ticketRepository,
                         AcquirenteRepository acquirenteRepository) {
        this.eventiRepository = eventiRepository;
        this.ticketRepository = ticketRepository;
        this.acquirenteRepository = acquirenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Evento> lista() {
        return eventiRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Evento get(Long id) {
        return eventiRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Evento crea(Evento e) {
        return eventiRepository.save(e);
    }

    @Transactional
    public Evento aggiorna(Long id, Evento update) {
        Evento e = eventiRepository.findById(id).orElseThrow();
        e.setNome(update.getNome());
        e.setTipo(update.getTipo());
        e.setPrezzo(update.getPrezzo());
        e.setPosti(update.getPosti());
        e.setDataOra(update.getDataOra());
        return eventiRepository.save(e);
    }

    @Transactional
    public void elimina(Long id) {
        eventiRepository.deleteById(id);
    }

    @Transactional
    public TicketEvento acquistaTicket(Long eventoId, Long acquirenteId, int quantita) {
        Evento evento = eventiRepository.findById(eventoId).orElseThrow();
        Acquirente acquirente = acquirenteRepository.findById(acquirenteId).orElseThrow();

        if (evento.getPosti() < quantita) {
            throw new IllegalArgumentException("Posti insufficienti");
        }

        TicketEvento ticket = new TicketEvento(evento, acquirente);
        ticket.setNumeroTicket(quantita);

        evento.setPosti(evento.getPosti() - quantita);
        eventiRepository.save(evento);

        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketEvento> listaTicketEvento(Long eventoId) {
        return ticketRepository.findByEventoId(eventoId);
    }
}