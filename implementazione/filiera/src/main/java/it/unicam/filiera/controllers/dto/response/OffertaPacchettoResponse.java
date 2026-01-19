package it.unicam.filiera.controllers.dto.response;

import it.unicam.filiera.domain.OffertaPacchetto;

public class OffertaPacchettoResponse {

    private Long id;
    private Long distributoreId;
    private Long pacchettoId;
    private double prezzoVendita;
    private int disponibilita;
    private boolean attiva;

    public OffertaPacchettoResponse() {}

    public static OffertaPacchettoResponse from(OffertaPacchetto o) {
        OffertaPacchettoResponse r = new OffertaPacchettoResponse();
        r.id = o.getId();
        r.distributoreId = o.getDistributore().getId();
        r.pacchettoId = o.getPacchetto().getId();
        r.prezzoVendita = o.getPrezzoVendita();
        r.disponibilita = o.getDisponibilita();
        r.attiva = o.isAttiva();
        return r;
    }

    public Long getId() { return id; }
    public Long getDistributoreId() { return distributoreId; }
    public Long getPacchettoId() { return pacchettoId; }
    public double getPrezzoVendita() { return prezzoVendita; }
    public int getDisponibilita() { return disponibilita; }
    public boolean isAttiva() { return attiva; }
}
