package it.unicam.filiera.controllers.dto;

import it.unicam.filiera.models.TipoCertificatore;

public class CertificatoDTO {

    public TipoCertificatore tipo; // tipo di certificatore
    public Long prodottoId;

    // campi Produttore
    public String azienda;
    public String origineMateriaPrima;

    // campi Trasformatore
    public String processo;
    public String impianto;

    // campi Curatore
    public Boolean approvato;
    public String commento;
}