package it.unicam.filiera.builder;

import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.utilities.CoordinateOSM;

public interface AziendeBuilder {

    AziendeBuilder setNomeAzienda(String nome);
    AziendeBuilder setSede(String sede);
    AziendeBuilder setCoordinate(CoordinateOSM coordinate);
    AziendeBuilder setPartitaIva(String partitaIva);

    Azienda build();
}