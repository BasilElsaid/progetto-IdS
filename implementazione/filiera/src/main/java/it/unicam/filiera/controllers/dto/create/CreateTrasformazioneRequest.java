package it.unicam.filiera.controllers.dto.create;

public class CreateTrasformazioneRequest {
    private Long processoId;
    private Long trasformatoreId;
    private Long inputId;
    private Long outputId;
    private Double quantitaInput;
    private Double quantitaOutput;
    private String note;

    public CreateTrasformazioneRequest() {}

    public Long getProcessoId() { return processoId; }
    public void setProcessoId(Long processoId) { this.processoId = processoId; }

    public Long getTrasformatoreId() { return trasformatoreId; }
    public void setTrasformatoreId(Long trasformatoreId) { this.trasformatoreId = trasformatoreId; }

    public Long getInputId() { return inputId; }
    public void setInputId(Long inputId) { this.inputId = inputId; }

    public Long getOutputId() { return outputId; }
    public void setOutputId(Long outputId) { this.outputId = outputId; }

    public Double getQuantitaInput() { return quantitaInput; }
    public void setQuantitaInput(Double quantitaInput) { this.quantitaInput = quantitaInput; }

    public Double getQuantitaOutput() { return quantitaOutput; }
    public void setQuantitaOutput(Double quantitaOutput) { this.quantitaOutput = quantitaOutput; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
