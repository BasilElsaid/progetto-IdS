package it.unicam.filiera.controllers.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LottoResponse {

    private Long id;
    private String qrCode;

    private Long prodottoId;
    private String prodottoNome;
    private String prodottoCategoria;

    private Long aziendaId;
    private String aziendaNome;

    private Double quantita;
    private String unitaMisura;

    private LocalDate dataRaccolta;
    private LocalDateTime creatoIl;
    private String note;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public Long getProdottoId() { return prodottoId; }
    public void setProdottoId(Long prodottoId) { this.prodottoId = prodottoId; }

    public String getProdottoNome() { return prodottoNome; }
    public void setProdottoNome(String prodottoNome) { this.prodottoNome = prodottoNome; }

    public String getProdottoCategoria() { return prodottoCategoria; }
    public void setProdottoCategoria(String prodottoCategoria) { this.prodottoCategoria = prodottoCategoria; }

    public Long getAziendaId() { return aziendaId; }
    public void setAziendaId(Long aziendaId) { this.aziendaId = aziendaId; }

    public String getAziendaNome() { return aziendaNome; }
    public void setAziendaNome(String aziendaNome) { this.aziendaNome = aziendaNome; }

    public Double getQuantita() { return quantita; }
    public void setQuantita(Double quantita) { this.quantita = quantita; }

    public String getUnitaMisura() { return unitaMisura; }
    public void setUnitaMisura(String unitaMisura) { this.unitaMisura = unitaMisura; }

    public LocalDate getDataRaccolta() { return dataRaccolta; }
    public void setDataRaccolta(LocalDate dataRaccolta) { this.dataRaccolta = dataRaccolta; }

    public LocalDateTime getCreatoIl() { return creatoIl; }
    public void setCreatoIl(LocalDateTime creatoIl) { this.creatoIl = creatoIl; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
