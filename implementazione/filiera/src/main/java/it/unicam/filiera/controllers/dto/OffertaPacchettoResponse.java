package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.domain.OffertaPacchetto;

public class OffertaPacchettoResponse {
    public Long id;
    public Long distributoreId;
    public Long pacchettoId;
    public double prezzoVendita;
    public int disponibilita;
    public boolean attiva;

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
}