package it.unicam.filiera.controllers.dto;

public class CreateAnnuncioMarketplaceRequest {
    public Long aziendaId;
    public Long prodottoId;
    public double prezzo;
    public int stock;
    public boolean attivo = true;
}