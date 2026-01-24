package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.Pacchetto;

import java.util.List;

public record PacchettoResponse(
        Long id,
        String nome,
        List<ProdottoInfo> prodotti,
        DistributoreInfo distributore
) {

    public record ProdottoInfo(Long id, String nome) {}
    public record DistributoreInfo(Long id, String nomeAzienda) {}

    public static PacchettoResponse from(Pacchetto p) {
        List<ProdottoInfo> prodotti = null;
        if (p.getProdotti() != null) {
            prodotti = p.getProdotti().stream()
                    .map(prod -> new ProdottoInfo(prod.getId(), prod.getNome()))
                    .toList();
        }

        DistributoreInfo distributore = null;
        if (p.getDistributore() != null) {
            distributore = new DistributoreInfo(
                    p.getDistributore().getId(),
                    p.getDistributore().getNomeAzienda()
            );
        }

        return new PacchettoResponse(
                p.getId(),
                p.getNome(),
                prodotti,
                distributore
        );
    }
}