package it.unicam.filiera.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "distributori")
public class DistributoreTipicita extends UtenteGenerico {
    private String areaDistribuzione;
}
