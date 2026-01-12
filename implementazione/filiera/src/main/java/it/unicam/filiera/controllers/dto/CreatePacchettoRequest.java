package it.unicam.filiera.controllers.dto;

public class CreatePacchettoRequest {

    private String nome;
    private double prezzo;

    public String getNome() { return nome; }
    public double getPrezzo() { return prezzo; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
}
