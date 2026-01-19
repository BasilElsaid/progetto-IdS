package it.unicam.filiera.controllers.dto.response;

import java.time.LocalDateTime;

public class TimelineItemResponse {

    private String tipo; // PRODUZIONE | TRASFORMAZIONE | VENDITA
    private LocalDateTime dataOra;
    private String descrizione;

    private String refTipo; // Lotto | Trasformazione | Ordine
    private Long refId;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getRefTipo() { return refTipo; }
    public void setRefTipo(String refTipo) { this.refTipo = refTipo; }

    public Long getRefId() { return refId; }
    public void setRefId(Long refId) { this.refId = refId; }
}
