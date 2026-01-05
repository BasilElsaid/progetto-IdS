package it.unicam.filiera.prodotto;

import jakarta.persistence.*;

@Entity
public class Certificazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    public Certificazione() {}
    public Certificazione(String nome) { this.nome = nome; }

    public Long getId() { return id; }
    public String getNome() { return nome; }
}
