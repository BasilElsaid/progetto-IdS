package it.unicam.filiera.domain;

import it.unicam.filiera.models.Azienda;
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
    @JoinColumn(name = "azienda_id", nullable = false)
    private Azienda proprietario;

    @Column(nullable = false)
    private boolean vendibile = false;
    private boolean isTrasformato = false;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public Double getPrezzo() { return prezzo; }
    public Azienda getProprietario() { return proprietario; }
    public boolean getVendibile() { return vendibile; }
    public boolean getIsTrasformato() { return isTrasformato; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }
    public void setProprietario(Azienda proprietario) { this.proprietario = proprietario; }
    public void setVendibile(boolean vendibile) { this.vendibile = vendibile; }
    public void setIsTrasformato(boolean isTrasformato) { this.isTrasformato = isTrasformato; }

}
