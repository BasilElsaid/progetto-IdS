package it.unicam.filiera.controllers.dto;

/**
 * Richiesta di acquisto ticket per un evento.
 * Se non specificata, la quantita' e' 1.
 */
public class AcquistaTicketRequest {

    private Integer quantita;

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
