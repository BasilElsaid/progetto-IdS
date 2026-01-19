package it.unicam.filiera.domain;

import it.unicam.filiera.models.Azienda;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lotti")
public class Lotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String qrCode;

    @ManyToOne(optional = false)
    private Prodotto prodotto;

    @ManyToOne(optional = false)
    private Azienda azienda;

    @Column(nullable = false)
    private Double quantita;

    @Column(nullable = false)
    private String unitaMisura = "kg";

    @Column(nullable = false)
    private LocalDate dataRaccolta;

    @Column(nullable = false)
    private LocalDateTime creatoIl = LocalDateTime.now();

    private String note;

    protected Lotto() {}

    public Lotto(String qrCode,
                 Prodotto prodotto,
                 Azienda azienda,
                 Double quantita,
                 String unitaMisura,
                 LocalDate dataRaccolta,
                 String note) {
        this.qrCode = qrCode;
        this.prodotto = prodotto;
        this.azienda = azienda;
        this.quantita = quantita;
        if (unitaMisura != null && !unitaMisura.isBlank()) {
            this.unitaMisura = unitaMisura;
        }
        this.dataRaccolta = dataRaccolta;
        this.note = note;
        this.creatoIl = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getQrCode() { return qrCode; }
    public Prodotto getProdotto() { return prodotto; }
    public Azienda getAzienda() { return azienda; }
    public Double getQuantita() { return quantita; }
    public String getUnitaMisura() { return unitaMisura; }
    public LocalDate getDataRaccolta() { return dataRaccolta; }
    public LocalDateTime getCreatoIl() { return creatoIl; }
    public String getNote() { return note; }

    public void setId(Long id) { this.id = id; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }
    public void setAzienda(Azienda azienda) { this.azienda = azienda; }
    public void setQuantita(Double quantita) { this.quantita = quantita; }
    public void setUnitaMisura(String unitaMisura) { this.unitaMisura = unitaMisura; }
    public void setDataRaccolta(LocalDate dataRaccolta) { this.dataRaccolta = dataRaccolta; }
    public void setCreatoIl(LocalDateTime creatoIl) { this.creatoIl = creatoIl; }
    public void setNote(String note) { this.note = note; }
}
