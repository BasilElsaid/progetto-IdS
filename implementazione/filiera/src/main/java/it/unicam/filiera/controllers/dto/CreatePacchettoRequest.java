package it.unicam.filiera.controllers.dto;

import java.util.List;

public class CreatePacchettoRequest {
    private String nome;
    private double prezzo;
    private List<Long> prodottiIds;

    public CreatePacchettoRequest() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public List<Long> getProdottiIds() { return prodottiIds; }
    public void setProdottiIds(List<Long> prodottiIds) { this.prodottiIds = prodottiIds; }
}
