package it.unicam.filiera.domain;

import jakarta.persistence.*;

@Entity
public class ProcessoTrasformazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descrizione;
    private boolean chiuso = false;

    public ProcessoTrasformazione() {}

    public ProcessoTrasformazione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getId() { return id; }
    public String getDescrizione() { return descrizione; }
    public boolean isChiuso() { return chiuso; }

    public void chiudi() { this.chiuso = true; }
}
