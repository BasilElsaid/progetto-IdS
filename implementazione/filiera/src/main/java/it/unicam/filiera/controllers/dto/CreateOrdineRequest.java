package it.unicam.filiera.controllers.dto;

import java.util.List;

public class CreateOrdineRequest {

    private Long acquirenteId;
    private List<Long> pacchettoIds;

    public CreateOrdineRequest() {}

    public Long getAcquirenteId() {
        return acquirenteId;
    }

    public void setAcquirenteId(Long acquirenteId) {
        this.acquirenteId = acquirenteId;
    }

    public List<Long> getPacchettoIds() {
        return pacchettoIds;
    }

    public void setPacchettoIds(List<Long> pacchettoIds) {
        this.pacchettoIds = pacchettoIds;
    }
}
