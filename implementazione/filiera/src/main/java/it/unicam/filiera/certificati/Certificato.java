package it.unicam.filiera.certificati;

import it.unicam.filiera.models.Prodotto;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Certificato implements StrategieCertificazioni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;

    public Long getId() { return id; }

    public Prodotto getProdotto() { return prodotto; }

    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }
}