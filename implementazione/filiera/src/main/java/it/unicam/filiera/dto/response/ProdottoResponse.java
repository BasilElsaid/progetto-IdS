package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.Prodotto;

public record ProdottoResponse(
        Long id,
        String nome,
        String categoria,
        Long produttoreId,
        String nomeAzienda
) {

    public static ProdottoResponse from(Prodotto p) {
        return new ProdottoResponse(
                p.getId(),
                p.getNome(),
                p.getCategoria(),
                p.getProprietario() != null ? p.getProprietario().getId() : null,
                p.getProprietario() != null ? p.getProprietario().getNomeAzienda() : null
        );
    }
}