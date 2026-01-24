package it.unicam.filiera.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreatePacchettoRequest {

    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;


    @NotEmpty(message = "Il pacchetto deve contenere almeno un prodotto")
    private List<Long> prodottiIds;

    public CreatePacchettoRequest() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Long> getProdottiIds() { return prodottiIds; }
    public void setProdottiIds(List<Long> prodottiIds) { this.prodottiIds = prodottiIds; }
}
