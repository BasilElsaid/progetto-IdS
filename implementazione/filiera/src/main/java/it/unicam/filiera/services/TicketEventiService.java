package it.unicam.filiera.services;

import it.unicam.filiera.dto.response.TicketEventoResponse;
import it.unicam.filiera.domain.Evento;
import it.unicam.filiera.domain.TicketEvento;
import it.unicam.filiera.enums.Ruolo;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TicketEventiService {

    private final EventiRepository eventiRepository;
    private final TicketEventoRepository ticketRepository;

    private final SecureRandom random = new SecureRandom();

    private final UtentiRepository utentiRepository;

    public TicketEventiService(
            EventiRepository eventiRepository,
            TicketEventoRepository ticketRepository,
            UtentiRepository utentiRepository
    ) {
        this.eventiRepository = eventiRepository;
        this.ticketRepository = ticketRepository;
        this.utentiRepository = utentiRepository;
    }

    public List<TicketEventoResponse> prendiTicket(Long eventoId, int quantita) {
        if (quantita <= 0) {
            throw new BadRequestException("La quantita deve essere maggiore di 0");
        }

        UtenteGenerico proprietario = getUtenteLoggato();

        Evento evento = eventiRepository.findById(eventoId)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        if (evento.getPosti() < quantita) {
            throw new BadRequestException("Posti insufficienti per l'acquisto richiesto");
        }

        List<TicketEventoResponse> result = new ArrayList<>();
        for (int i = 0; i < quantita; i++) {
            TicketEvento t = new TicketEvento(evento, proprietario);
            t.setNumeroTicket(generaNumeroTicketUnico());
            t.setQrCode("TCK-" + UUID.randomUUID().toString().replace("-", ""));
            evento.aggiungiBiglietto(t); // scala 1 posto
            ticketRepository.save(t);
            result.add(TicketEventoResponse.from(t));
        }
        return result;
    }

    public List<TicketEventoResponse> listaMieiTicket() {
        UtenteGenerico utente = getUtenteLoggato();
        return ticketRepository.findByProprietario_Id(utente.getId())
                .stream()
                .map(TicketEventoResponse::from)
                .toList();
    }

    public List<TicketEventoResponse> listaTicketEvento(Long eventoId) {
        UtenteGenerico u = getUtenteLoggato();
        if (u.getRuolo() != Ruolo.ANIMATORE && u.getRuolo() != Ruolo.GESTORE_PIATTAFORMA) {
            throw new ForbiddenException("Ruolo non autorizzato");
        }
        if (!eventiRepository.existsById(eventoId)) {
            throw new NotFoundException("Evento non trovato");
        }
        return ticketRepository.findByEventoId(eventoId)
                .stream()
                .map(TicketEventoResponse::from)
                .toList();
    }

    public TicketEventoResponse checkIn(int numeroTicket) {
        UtenteGenerico operatore = getUtenteLoggato();

        TicketEvento ticket = ticketRepository.findByNumeroTicket(numeroTicket)
                .orElseThrow(() -> new NotFoundException("Ticket non trovato"));

        if (ticket.isUsato()) {
            throw new BadRequestException("Ticket gia utilizzato");
        }

        ticket.setUsato(true);
        ticket.setUsatoIl(LocalDateTime.now());
        ticket.setCheckInDa(operatore);

        return TicketEventoResponse.from(ticket);    }

    public void annullaTicket(Long ticketId) {
        UtenteGenerico u = getUtenteLoggato();

        TicketEvento ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket non trovato"));

        boolean isGestore = u.getRuolo() == Ruolo.GESTORE_PIATTAFORMA;
        boolean isOwner = ticket.getProprietario().getId().equals(u.getId());

        if (!isGestore && !isOwner) {
            throw new ForbiddenException("Ruolo non autorizzato");
        }

        if (ticket.isUsato()) {
            throw new BadRequestException("Non puoi annullare un ticket già utilizzato");
        }

        Evento evento = ticket.getEvento();
        evento.setPosti(evento.getPosti() + 1);

        ticketRepository.delete(ticket);
    }

    // ================== HELPERS ==================

    private int generaNumeroTicketUnico() {
        for (int i = 0; i < 20; i++) {
            int candidate = 100_000 + random.nextInt(900_000);
            if (!ticketRepository.existsByNumeroTicket(candidate)) {
                return candidate;
            }
        }
        int candidate = (int) (System.currentTimeMillis() % 1_000_000);
        if (candidate < 100_000) candidate += 100_000;
        while (ticketRepository.existsByNumeroTicket(candidate)) {
            candidate = (candidate + 1) % 1_000_000;
            if (candidate < 100_000) candidate = 100_000;
        }
        return candidate;
    }

    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }

        return utentiRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
    }

}
