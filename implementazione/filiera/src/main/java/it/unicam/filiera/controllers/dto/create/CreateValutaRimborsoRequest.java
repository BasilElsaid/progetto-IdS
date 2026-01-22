package it.unicam.filiera.controllers.dto.create;

public class CreateValutaRimborsoRequest {
    private boolean approva;
    private String notaGestore;

    public boolean isApprova() { return approva; }
    public void setApprova(boolean approva) { this.approva = approva; }

    public String getNotaGestore() { return notaGestore; }
    public void setNotaGestore(String notaGestore) { this.notaGestore = notaGestore; }
}