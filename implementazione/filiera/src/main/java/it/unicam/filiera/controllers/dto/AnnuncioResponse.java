package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.AnnuncioMarketplace;

import java.time.LocalDateTime;

public class AnnuncioResponse {
    private Long id;
    private Long aziendaId;
    private Long prodottoId;
    private String categoria;
    private String nomeProdotto;
    private double prezzo;
    private int stock;
    private boolean attivo;
    private LocalDateTime creatoIl;

    public AnnuncioResponse() {}

    public AnnuncioResponse(Long id, Long aziendaId, Long prodottoId, String categoria, String nomeProdotto,
                            double prezzo, int stock, boolean attivo, LocalDateTime creatoIl) {
        this.id = id;
        this.aziendaId = aziendaId;
        this.prodottoId = prodottoId;
        this.categoria = categoria;
        this.nomeProdotto = nomeProdotto;
        this.prezzo = prezzo;
        this.stock = stock;
        this.attivo = attivo;
        this.creatoIl = creatoIl;
    }

    public static AnnuncioResponse from(AnnuncioMarketplace a) {
        return new AnnuncioResponse(
                a.getId(),
                a.getAzienda().getId(),
                a.getProdotto().getId(),
                a.getProdotto().getCategoria(),
                a.getProdotto().getNome(),
                a.getPrezzo(),
                a.getStock(),
                a.isAttivo(),
                a.getCreatoIl()
        );
    }

    public Long getId() { return id; }
    public Long getAziendaId() { return aziendaId; }
    public Long getProdottoId() { return prodottoId; }
    public String getCategoria() { return categoria; }
    public String getNomeProdotto() { return nomeProdotto; }
    public double getPrezzo() { return prezzo; }
    public int getStock() { return stock; }
    public boolean isAttivo() { return attivo; }
    public LocalDateTime getCreatoIl() { return creatoIl; }
}