package it.unicam.filiera.dto.response;


public class ProdottoResponse {

    private Long id;
    private String nome;
    private String categoria;
    private double prezzo;

    private Long produttoreId;
    private String nomeAzienda;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public Long getProduttoreId() {
        return produttoreId;
    }

    public void setProduttoreId(Long produttoreId) {
        this.produttoreId = produttoreId;
    }

    public String getNomeAzienda() {
        return nomeAzienda;
    }

    public void setNomeAzienda(String nomeAzienda) {
        this.nomeAzienda = nomeAzienda;
    }

}