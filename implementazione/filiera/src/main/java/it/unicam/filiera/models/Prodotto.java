package it.unicam.filiera.models;

import jakarta.persistence.*;

@Entity
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String categoria;
    private Double prezzo;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public Double getPrezzo() { return prezzo; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }
}
