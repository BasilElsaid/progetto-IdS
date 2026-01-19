package it.unicam.filiera.controllers.dto;

import java.time.LocalDate;

public class CreateLottoRequest {

    private Long prodottoId;
    private Long aziendaId;

    private Double quantita;
    private String unitaMisura;

    private LocalDate dataRaccolta;
    private String note;

    public Long getProdottoId() { return prodottoId; }
    public void setProdottoId(Long prodottoId) { this.prodottoId = prodottoId; }

    public Long getAziendaId() { return aziendaId; }
    public void setAziendaId(Long aziendaId) { this.aziendaId = aziendaId; }

    public Double getQuantita() { return quantita; }
    public void setQuantita(Double quantita) { this.quantita = quantita; }

    public String getUnitaMisura() { return unitaMisura; }
    public void setUnitaMisura(String unitaMisura) { this.unitaMisura = unitaMisura; }

    public LocalDate getDataRaccolta() { return dataRaccolta; }
    public void setDataRaccolta(LocalDate dataRaccolta) { this.dataRaccolta = dataRaccolta; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
