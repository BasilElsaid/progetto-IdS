package it.unicam.filiera.prodotto;

import jakarta.persistence.*;

@Entity
public class Pacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private double prezzo;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public double getPrezzo() { return prezzo; }

    public void setNome(String nome) { this.nome = nome; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
}
