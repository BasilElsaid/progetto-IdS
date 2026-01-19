package it.unicam.filiera.controllers.dto.response;

public class EventoStatsResponse {
    private Long eventoId;
    private int postiTotali;
    private int postiResidui;
    private long ticketsVenduti;
    private long checkInFatti;

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public int getPostiTotali() { return postiTotali; }
    public void setPostiTotali(int postiTotali) { this.postiTotali = postiTotali; }

    public int getPostiResidui() { return postiResidui; }
    public void setPostiResidui(int postiResidui) { this.postiResidui = postiResidui; }

    public long getTicketsVenduti() { return ticketsVenduti; }
    public void setTicketsVenduti(long ticketsVenduti) { this.ticketsVenduti = ticketsVenduti; }

    public long getCheckInFatti() { return checkInFatti; }
    public void setCheckInFatti(long checkInFatti) { this.checkInFatti = checkInFatti; }
}
