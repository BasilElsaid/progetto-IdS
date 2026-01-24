package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.TicketEvento;

import java.time.LocalDateTime;

public record TicketEventoResponse(
        Long id,
        int numeroTicket,
        String qrCode,
        Long eventoId,
        String eventoNome,
        LocalDateTime eventoDataOra,
        Long proprietarioId,
        LocalDateTime acquistatoIl,
        boolean usato,
        LocalDateTime usatoIl,
        Long checkInDaId,
        String checkInDaUsername
) {

    public static TicketEventoResponse from(TicketEvento t) {
        return new TicketEventoResponse(
                t.getId(),
                t.getNumeroTicket(),
                t.getQrCode(),
                t.getEvento().getId(),
                t.getEvento().getNome(),
                t.getEvento().getDataOra(),
                t.getProprietario().getId(),
                t.getAcquistatoIl(),
                t.isUsato(),
                t.getUsatoIl(),
                t.getCheckInDa() != null ? t.getCheckInDa().getId() : null,
                t.getCheckInDa() != null ? t.getCheckInDa().getUsername() : null
        );
    }
}