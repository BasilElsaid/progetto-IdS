package it.unicam.filiera.domain;

import it.unicam.filiera.models.Trasformatore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TrasformazioneProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Trasformatore trasformatore;

    @ManyToOne(optional = false)
    private Prodotto inputProdotto;

    @ManyToOne(optional = false)
    private Prodotto outputProdotto;

    private String note;

    private LocalDateTime creatoIl = LocalDateTime.now();

    protected TrasformazioneProdotto() {}

    public TrasformazioneProdotto(Trasformatore trasformatore, Prodotto inputProdotto, Prodotto outputProdotto, String note) {
        this.trasformatore = trasformatore;
        this.inputProdotto = inputProdotto;
        this.outputProdotto = outputProdotto;
        this.note = note;
        this.creatoIl = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trasformatore getTrasformatore() {
        return trasformatore;
    }

    public void setTrasformatore(Trasformatore trasformatore) {
        this.trasformatore = trasformatore;
    }

    public Prodotto getInputProdotto() {
        return inputProdotto;
    }

    public void setInputProdotto(Prodotto inputProdotto) {
        this.inputProdotto = inputProdotto;
    }

    public Prodotto getOutputProdotto() {
        return outputProdotto;
    }

    public void setOutputProdotto(Prodotto outputProdotto) {
        this.outputProdotto = outputProdotto;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatoIl() {
        return creatoIl;
    }

    public void setCreatoIl(LocalDateTime creatoIl) {
        this.creatoIl = creatoIl;
    }
}
