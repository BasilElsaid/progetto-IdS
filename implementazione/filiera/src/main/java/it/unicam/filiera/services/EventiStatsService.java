package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.EventoStatsResponse;
import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.repositories.EventiRepository;
import it.unicam.filiera.repositories.TicketEventoRepository;
import org.springframework.stereotype.Service;

@Service
public class EventiStatsService {

    private final EventiRepository eventiRepo;
    private final TicketEventoRepository ticketRepo;

    public EventiStatsService(EventiRepository eventiRepo, TicketEventoRepository ticketRepo) {
        this.eventiRepo = eventiRepo;
        this.ticketRepo = ticketRepo;
    }

    public EventoStatsResponse stats(Long eventoId) {
        Evento e = eventiRepo.findById(eventoId)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        long venduti = ticketRepo.countByEventoId(eventoId);
        long checkin = ticketRepo.countByEventoIdAndUsatoTrue(eventoId);

        EventoStatsResponse r = new EventoStatsResponse();
        r.setEventoId(eventoId);
        r.setPostiResidui(e.getPosti());
        r.setTicketsVenduti(venduti);
        r.setCheckInFatti(checkin);
        r.setPostiTotali((int) venduti + e.getPosti());
        return r;
    }
}
