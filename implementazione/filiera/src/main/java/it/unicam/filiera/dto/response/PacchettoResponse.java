package it.unicam.filiera.dto.response;

import java.util.List;

public class PacchettoResponse {

    private Long id;
    private String nome;
    private List<ProdottoInfo> prodotti;
    private DistributoreInfo distributore;

    public record ProdottoInfo(Long id, String nome) {}
    public record DistributoreInfo(Long id, String nomeAzienda) {}

    // ======== GETTER/SETTER ========
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<ProdottoInfo> getProdotti() { return prodotti; }
    public void setProdotti(List<ProdottoInfo> prodotti) { this.prodotti = prodotti; }

    public DistributoreInfo getDistributore() { return distributore; }
    public void setDistributore(DistributoreInfo distributore) { this.distributore = distributore; }
}