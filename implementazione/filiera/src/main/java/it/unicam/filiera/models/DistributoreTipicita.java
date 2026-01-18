package it.unicam.filiera.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "distributori")
public class DistributoreTipicita extends Azienda {

    private String areaDistribuzione;

    public DistributoreTipicita() {}

    public String getAreaDistribuzione() { return areaDistribuzione; }
    public void setAreaDistribuzione(String areaDistribuzione) { this.areaDistribuzione = areaDistribuzione; }
}
