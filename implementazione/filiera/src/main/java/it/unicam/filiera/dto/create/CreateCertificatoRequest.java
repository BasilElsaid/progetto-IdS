package it.unicam.filiera.dto.create;

import it.unicam.filiera.enums.TipoCertificatore;

import jakarta.validation.constraints.NotNull;

public class CreateCertificatoRequest {

    @NotNull
    public TipoCertificatore tipo; // tipo di certificatore

    @NotNull
    public Long prodottoId;
    public String azienda;

    // campi Produttore
    public String origineMateriaPrima;

    // campi Trasformatore
    public String processo;
    public String impianto;
}