package it.unicam.filiera.controllers.dto;

import java.util.List;

public class CreateOrdineRequest {

    private Long acquirenteId;
    private List<Long> pacchettoIds;

    public Long getAcquirenteId() {
        return acquirenteId;
    }

    public List<Long> getPacchettoIds() {
        return pacchettoIds;
    }
}
