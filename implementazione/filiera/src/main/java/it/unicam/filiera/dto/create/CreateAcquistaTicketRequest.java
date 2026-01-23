package it.unicam.filiera.dto.create;

/**
 * Richiesta di acquisto ticket per un evento.
 * Se non specificata, la quantita' e' 1.
 */
public class CreateAcquistaTicketRequest {

    private Integer quantita;

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
