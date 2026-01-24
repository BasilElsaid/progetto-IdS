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
                p.getProduttore() != null ? p.getProduttore().getId() : null,
                p.getProduttore() != null ? p.getProduttore().getNomeAzienda() : null
        );
    }
}