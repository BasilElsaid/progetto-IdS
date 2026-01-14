package it.unicam.filiera.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "produttori")
public class Produttore extends UtenteGenerico {

    private String nomeAzienda;
    private String partitaIva;

    public Produttore() {
        setRuolo(Ruolo.PRODUTTORE);
    }

    public String getNomeAzienda() { return nomeAzienda; }
    public void setNomeAzienda(String nomeAzienda) { this.nomeAzienda = nomeAzienda; }

    public String getPartitaIva() { return partitaIva; }
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }
}
