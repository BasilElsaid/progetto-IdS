package it.unicam.filiera.models;

import it.unicam.filiera.enums.Ruolo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "distributori")
public class DistributoreTipicita extends Azienda {

    private String areaDistribuzione;

    public DistributoreTipicita() {
        setRuolo(Ruolo.DISTRIBUTORE_TIPICITA);
    }

    public String getAreaDistribuzione() { return areaDistribuzione; }
    public void setAreaDistribuzione(String areaDistribuzione) { this.areaDistribuzione = areaDistribuzione; }
}
