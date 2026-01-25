package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.enums.TipoCertificatore;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Certificato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "prodotto_id")
    protected Prodotto prodotto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected TipoCertificatore tipo;

    public Long getId() { return id; }

    public Prodotto getProdotto() { return prodotto; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }

    public TipoCertificatore getTipo() { return tipo; }
    public void setTipo(TipoCertificatore tipo) { this.tipo = tipo; }
}