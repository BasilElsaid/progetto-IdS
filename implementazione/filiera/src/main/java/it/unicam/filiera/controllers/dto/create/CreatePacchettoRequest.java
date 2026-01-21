package it.unicam.filiera.controllers.dto.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreatePacchettoRequest {

    @NotNull(message = "Il nome non può essere nullo")
    private String nome;

    private double prezzo;

    @NotEmpty(message = "Il pacchetto deve contenere almeno un prodotto")
    private List<Long> prodottiIds;

    public CreatePacchettoRequest() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public List<Long> getProdottiIds() { return prodottiIds; }
    public void setProdottiIds(List<Long> prodottiIds) { this.prodottiIds = prodottiIds; }
}
