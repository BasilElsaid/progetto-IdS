package it.unicam.filiera.controllers.dto;

import java.util.List;

public class CreatePacchettoConProdottiRequest {
    private String nome;
    private double prezzo;
    private Long distributoreId;
    private List<Long> prodottiIds;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public Long getDistributoreId() { return distributoreId; }
    public void setDistributoreId(Long distributoreId) { this.distributoreId = distributoreId; }

    public List<Long> getProdottiIds() { return prodottiIds; }
    public void setProdottiIds(List<Long> prodottiIds) { this.prodottiIds = prodottiIds; }
}
