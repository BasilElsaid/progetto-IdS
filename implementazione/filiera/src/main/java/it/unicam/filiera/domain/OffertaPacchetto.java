package it.unicam.filiera.domain;

import jakarta.persistence.*;
import it.unicam.filiera.models.DistributoreTipicita;
import it.unicam.filiera.domain.Pacchetto;

@Entity
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
    private boolean attiva = true;

    public OffertaPacchetto() {}

    public Long getId() { return id; }

    public DistributoreTipicita getDistributore() { return distributore; }
    public void setDistributore(DistributoreTipicita distributore) { this.distributore = distributore; }

    public Pacchetto getPacchetto() { return pacchetto; }
    public void setPacchetto(Pacchetto pacchetto) { this.pacchetto = pacchetto; }

    public double getPrezzoVendita() { return prezzoVendita; }
    public void setPrezzoVendita(double prezzoVendita) { this.prezzoVendita = prezzoVendita; }

    public int getDisponibilita() { return disponibilita; }
    public void setDisponibilita(int disponibilita) { this.disponibilita = disponibilita; }

    public boolean isAttiva() { return attiva; }
    public void setAttiva(boolean attiva) { this.attiva = attiva; }
}