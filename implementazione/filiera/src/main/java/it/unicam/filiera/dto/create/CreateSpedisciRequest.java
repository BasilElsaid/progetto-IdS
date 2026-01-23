package it.unicam.filiera.dto.create;

public class CreateSpedisciRequest {

    private String trackingCode;

    public CreateSpedisciRequest() {
        // costruttore vuoto necessario per la deserializzazione JSON
    }

    public CreateSpedisciRequest(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }
}