package it.unicam.filiera.models;

import jakarta.persistence.*;

@Entity
public class Azienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String sede;

    @Enumerated(EnumType.STRING)
    private TipoAzienda tipo;

    // ===== COSTRUTTORI =====
    public Azienda() {}

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSede() {
        return sede;
    }

    public TipoAzienda getTipo() {
        return tipo;
    }

    // ===== SETTERS =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public void setTipo(TipoAzienda tipo) {
        this.tipo = tipo;
    }
}
