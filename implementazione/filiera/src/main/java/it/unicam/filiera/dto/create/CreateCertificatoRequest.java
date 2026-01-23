package it.unicam.filiera.dto.create;

import it.unicam.filiera.enums.TipoCertificatore;

import jakarta.validation.constraints.NotNull;

public class CreateCertificatoRequest {

    @NotNull
    public TipoCertificatore tipo; // tipo di certificatore

    @NotNull
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
    public Long certificatoTargetId;
}