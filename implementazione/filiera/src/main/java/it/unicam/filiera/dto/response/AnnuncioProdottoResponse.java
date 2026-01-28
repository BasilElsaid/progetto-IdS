package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.AnnuncioProdotto;

import java.time.LocalDateTime;

public record AnnuncioProdottoResponse(
        Long id,
        Long aziendaId,
        Long prodottoId,
        String nomeProdotto,
        double prezzo,
        int stock,
        boolean attivo,
        LocalDateTime creatoIl
) {
    public static AnnuncioProdottoResponse from(AnnuncioProdotto a) {
        return new AnnuncioProdottoResponse(
                a.getId(),
                a.getAzienda().getId(),
                a.getProdotto().getId(),
                a.getProdotto() != null ? a.getProdotto().getNome() : null,
                a.getPrezzo(),
                a.getStock(),
                a.isAttivo(),
                a.getCreatoIl()
        );
    }
}