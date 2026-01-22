package it.unicam.filiera.domain;

import it.unicam.filiera.models.Azienda;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "annunci_pacchetti")
public class AnnuncioPacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Azienda azienda;

    @ManyToOne(optional = false)
    private Pacchetto pacchetto;

    private double prezzo;
    private int stock;
    private boolean attivo;

    private LocalDateTime creatoIl = LocalDateTime.now();


    public AnnuncioPacchetto() {
        this.creatoIl = LocalDateTime.now();
        this.attivo = true;
    }

    public Long getId() { return id; }
    public Azienda getAzienda() { return azienda; }
    public Pacchetto getPacchetto() { return pacchetto; }
    public double getPrezzo() { return prezzo; }
    public int getStock() { return stock; }
    public boolean isAttivo() { return attivo; }
    public LocalDateTime getCreatoIl() { return creatoIl; }


    public void setAzienda(Azienda azienda) { this.azienda = azienda; }
    public void setPacchetto(Pacchetto pacchetto) { this.pacchetto = pacchetto; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
    public void setStock(int stock) { this.stock = stock; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }
}
