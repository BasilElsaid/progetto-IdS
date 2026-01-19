package it.unicam.filiera.controllers.dto;

import java.time.LocalDateTime;

public class TicketEventoResponse {

    private Long id;
    private int numeroTicket;

    private Long eventoId;
    private String eventoNome;
    private LocalDateTime eventoDataOra;

    private Long acquirenteId;

    private LocalDateTime acquistatoIl;
    private boolean usato;
    private LocalDateTime usatoIl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getNumeroTicket() { return numeroTicket; }
    public void setNumeroTicket(int numeroTicket) { this.numeroTicket = numeroTicket; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getEventoNome() { return eventoNome; }
    public void setEventoNome(String eventoNome) { this.eventoNome = eventoNome; }

    public LocalDateTime getEventoDataOra() { return eventoDataOra; }
    public void setEventoDataOra(LocalDateTime eventoDataOra) { this.eventoDataOra = eventoDataOra; }

    public Long getAcquirenteId() { return acquirenteId; }
    public void setAcquirenteId(Long acquirenteId) { this.acquirenteId = acquirenteId; }

    public LocalDateTime getAcquistatoIl() { return acquistatoIl; }
    public void setAcquistatoIl(LocalDateTime acquistatoIl) { this.acquistatoIl = acquistatoIl; }

    public boolean isUsato() { return usato; }
    public void setUsato(boolean usato) { this.usato = usato; }

    public LocalDateTime getUsatoIl() { return usatoIl; }
    public void setUsatoIl(LocalDateTime usatoIl) { this.usatoIl = usatoIl; }
}
