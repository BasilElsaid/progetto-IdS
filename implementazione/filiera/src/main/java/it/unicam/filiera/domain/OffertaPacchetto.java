package it.unicam.filiera.domain;

import it.unicam.filiera.models.DistributoreTipicita;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "offerte_pacchetti")
public class OffertaPacchetto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private DistributoreTipicita distributore;

    @ManyToOne(optional = false)
    private Pacchetto pacchetto;

    private double prezzoVendita;
    private int disponibilita;
    private boolean attiva;

    private LocalDateTime creatoIl = LocalDateTime.now();

    protected OffertaPacchetto() {
    }

    public OffertaPacchetto(DistributoreTipicita distributore, Pacchetto pacchetto, double prezzoVendita, int disponibilita, boolean attiva) {
        this.distributore = distributore;
        this.pacchetto = pacchetto;
        this.prezzoVendita = prezzoVendita;
        this.disponibilita = disponibilita;
        this.attiva = attiva;
        this.creatoIl = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public DistributoreTipicita getDistributore() { return distributore; }
    public Pacchetto getPacchetto() { return pacchetto; }
    public double getPrezzoVendita() { return prezzoVendita; }
    public int getDisponibilita() { return disponibilita; }
    public boolean isAttiva() { return attiva; }
    public LocalDateTime getCreatoIl() { return creatoIl; }

    public void setPrezzoVendita(double prezzoVendita) { this.prezzoVendita = prezzoVendita; }
    public void setDisponibilita(int disponibilita) { this.disponibilita = disponibilita; }
    public void setAttiva(boolean attiva) { this.attiva = attiva; }
}
