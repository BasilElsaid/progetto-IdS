package it.unicam.filiera.domain;

import it.unicam.filiera.models.Azienda;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "annunci_marketplace")
public class AnnuncioMarketplace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Azienda azienda;

    @ManyToOne(optional = false)
    private Prodotto prodotto;

    private double prezzo;
    private int stock;
    private boolean attivo;

    private LocalDateTime creatoIl;

    public AnnuncioMarketplace() {
        this.creatoIl = LocalDateTime.now();
        this.attivo = true;
    }

    public Long getId() { return id; }
    public Azienda getAzienda() { return azienda; }
    public Prodotto getProdotto() { return prodotto; }
    public double getPrezzo() { return prezzo; }
    public int getStock() { return stock; }
    public boolean isAttivo() { return attivo; }
    public LocalDateTime getCreatoIl() { return creatoIl; }

    public void setAzienda(Azienda azienda) { this.azienda = azienda; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
    public void setStock(int stock) { this.stock = stock; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }
}
