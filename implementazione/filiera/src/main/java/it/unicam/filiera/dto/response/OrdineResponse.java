package it.unicam.filiera.dto.response;

import it.unicam.filiera.domain.Ordine;
import it.unicam.filiera.enums.StatoOrdine;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrdineResponse {

    private Long id;
    private Long acquirenteId;
    private StatoOrdine stato;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataPagamento;
    private double totale;
    private List<OrdineItemResponse> items;

    public static OrdineResponse from(Ordine ordine) {
        OrdineResponse r = new OrdineResponse();
        r.id = ordine.getId();
        r.acquirenteId = ordine.getAcquirente().getId();
        r.stato = ordine.getStato();
        r.dataCreazione = ordine.getDataCreazione();
        r.dataPagamento = ordine.getDataPagamento();
        r.totale = ordine.getTotale();
        r.items = ordine.getItems().stream()
                .map(OrdineItemResponse::from)
                .collect(Collectors.toList());
        return r;
    }

    // getters e setters
    public Long getId() { return id; }
    public Long getAcquirenteId() { return acquirenteId; }
    public StatoOrdine getStato() { return stato; }
    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public double getTotale() { return totale; }
    public List<OrdineItemResponse> getItems() { return items; }

    public void setId(Long id) { this.id = id; }
    public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }
    public void setStato(StatoOrdine stato) { this.stato = stato; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
    public void setTotale(double totale) { this.totale = totale; }
    public void setItems(List<OrdineItemResponse> items) { this.items = items; }
}