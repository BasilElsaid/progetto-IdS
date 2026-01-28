package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.AnnuncioPacchetto;

import java.time.LocalDateTime;

public record AnnuncioPacchettoResponse(
        Long id,
        Long aziendaId,
        Long pacchettoId,
        String nomePacchetto,
        double prezzo,
        int stock,
        boolean attivo,
        LocalDateTime creatoIl
) {
    public static AnnuncioPacchettoResponse from(AnnuncioPacchetto a) {
        return new AnnuncioPacchettoResponse(
                a.getId(),
                a.getAzienda().getId(),
                a.getPacchetto().getId(),
                a.getPacchetto().getNome(),
                a.getPrezzo(),
                a.getStock(),
                a.isAttivo(),
                a.getCreatoIl()
        );
    }
}