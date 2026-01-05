package it.unicam.filiera.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Produttore extends UtenteGenerico {

    @Column(nullable = false, length = 120)
    private String ragioneSociale;

    @Column(nullable = false, unique = true, length = 20)
    private String partitaIva;

    @Column(nullable = false, length = 60)
    private String comune;

    @Column(nullable = false, length = 2)
    private String provincia; // es. "MC"

    @Column(nullable = false, length = 40)
    private String regione;

    // getters/setters
    public String getRagioneSociale() { return ragioneSociale; }
    public void setRagioneSociale(String ragioneSociale) { this.ragioneSociale = ragioneSociale; }

    public String getPartitaIva() { return partitaIva; }
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }

    public String getComune() { return comune; }
    public void setComune(String comune) { this.comune = comune; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getRegione() { return regione; }
    public void setRegione(String regione) { this.regione = regione; }
}
