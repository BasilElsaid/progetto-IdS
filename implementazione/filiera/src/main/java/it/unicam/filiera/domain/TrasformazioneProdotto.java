package it.unicam.filiera.domain;

import it.unicam.filiera.models.Trasformatore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trasformazioni_prodotto")
public class TrasformazioneProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private ProcessoTrasformazione processo;

    @ManyToOne(optional = false)
    private Trasformatore trasformatore;

    @ManyToOne(optional = false)
    private Prodotto inputProdotto;

    @ManyToOne(optional = false)
    private Prodotto outputProdotto;

    private Double quantitaInput;
    private Double quantitaOutput;

    private String note;

    private LocalDateTime creatoIl = LocalDateTime.now();

    protected TrasformazioneProdotto() {
    }

    public TrasformazioneProdotto(
            ProcessoTrasformazione processo,
            Trasformatore trasformatore,
            Prodotto inputProdotto,
            Prodotto outputProdotto,
            Double quantitaInput,
            Double quantitaOutput,
            String note
    ) {
        this.processo = processo;
        this.trasformatore = trasformatore;
        this.inputProdotto = inputProdotto;
        this.outputProdotto = outputProdotto;
        this.quantitaInput = quantitaInput;
        this.quantitaOutput = quantitaOutput;
        this.note = note;
        this.creatoIl = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public ProcessoTrasformazione getProcesso() { return processo; }
    public Trasformatore getTrasformatore() { return trasformatore; }
    public Prodotto getInputProdotto() { return inputProdotto; }
    public Prodotto getOutputProdotto() { return outputProdotto; }
    public Double getQuantitaInput() { return quantitaInput; }
    public Double getQuantitaOutput() { return quantitaOutput; }
    public String getNote() { return note; }
    public LocalDateTime getCreatoIl() { return creatoIl; }
}
