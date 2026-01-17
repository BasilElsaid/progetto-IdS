package it.unicam.filiera.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import it.unicam.filiera.models.Azienda;
import it.unicam.filiera.domain.Prodotto;

@Entity
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
    private boolean attivo = true;

    private LocalDateTime creatoIl = LocalDateTime.now();

    public AnnuncioMarketplace() {}

    public Long getId() { return id; }
    public Azienda getAzienda() { return azienda; }
    public void setAzienda(Azienda azienda) { this.azienda = azienda; }

    public Prodotto getProdotto() { return prodotto; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean isAttivo() { return attivo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }

    public LocalDateTime getCreatoIl() { return creatoIl; }
    public void setCreatoIl(LocalDateTime creatoIl) { this.creatoIl = creatoIl; }
}