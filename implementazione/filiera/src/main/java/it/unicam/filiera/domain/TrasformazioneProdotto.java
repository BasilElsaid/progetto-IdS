package it.unicam.filiera.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import it.unicam.filiera.domain.ProcessoTrasformazione;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.models.Trasformatore;

@Entity
public class TrasformazioneProdotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ProcessoTrasformazione processo;

    @ManyToOne(optional = false)
    private Prodotto prodottoInput;

    @ManyToOne(optional = false)
    private Prodotto prodottoOutput;

    @ManyToOne(optional = false)
    private Trasformatore trasformatore;

    private Double quantitaInput;
    private Double quantitaOutput;
    private String note;
    private LocalDateTime dataOra = LocalDateTime.now();

    public TrasformazioneProdotto() {}

    public Long getId() { return id; }

    public ProcessoTrasformazione getProcesso() { return processo; }
    public void setProcesso(ProcessoTrasformazione processo) { this.processo = processo; }

    public Prodotto getProdottoInput() { return prodottoInput; }
    public void setProdottoInput(Prodotto prodottoInput) { this.prodottoInput = prodottoInput; }

    public Prodotto getProdottoOutput() { return prodottoOutput; }
    public void setProdottoOutput(Prodotto prodottoOutput) { this.prodottoOutput = prodottoOutput; }

    public Trasformatore getTrasformatore() { return trasformatore; }
    public void setTrasformatore(Trasformatore trasformatore) { this.trasformatore = trasformatore; }

    public Double getQuantitaInput() { return quantitaInput; }
    public void setQuantitaInput(Double quantitaInput) { this.quantitaInput = quantitaInput; }

    public Double getQuantitaOutput() { return quantitaOutput; }
    public void setQuantitaOutput(Double quantitaOutput) { this.quantitaOutput = quantitaOutput; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
}