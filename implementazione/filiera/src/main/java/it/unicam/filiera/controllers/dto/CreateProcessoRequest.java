package it.unicam.filiera.controllers.dto;

import java.util.List;

public class CreateProcessoRequest {
    private Long trasformatoreId;
    private String descrizione;
    private List<Long> inputProdottiIds;
    private Long outputProdottoId; // opzionale: puoi crearlo dopo

    public Long getTrasformatoreId() { return trasformatoreId; }
    public void setTrasformatoreId(Long trasformatoreId) { this.trasformatoreId = trasformatoreId; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public List<Long> getInputProdottiIds() { return inputProdottiIds; }
    public void setInputProdottiIds(List<Long> inputProdottiIds) { this.inputProdottiIds = inputProdottiIds; }

    public Long getOutputProdottoId() { return outputProdottoId; }
    public void setOutputProdottoId(Long outputProdottoId) { this.outputProdottoId = outputProdottoId; }
}
