package it.unicam.filiera.domain;

import it.unicam.filiera.models.Produttore;
import jakarta.persistence.*;

@Entity
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String categoria;
    private Double prezzo;

    @ManyToOne
    @JoinColumn(name = "produttore_id", nullable = false)
    private Produttore produttore; // riferimento al produttore

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public Double getPrezzo() { return prezzo; }
    public Produttore getProduttore() { return produttore; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }
    public void setProduttore(Produttore produttore) { this.produttore = produttore; }

}
