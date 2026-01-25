package it.unicam.filiera.dto.update;

public record UpdateAnnuncioMarketplaceRequest(
        Double prezzo,
        Integer stock,
        Boolean attivo
) {}