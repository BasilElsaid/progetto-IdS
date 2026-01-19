package it.unicam.filiera.domain;

import it.unicam.filiera.models.Animatore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Evento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column
	protected String nome;

	@Column
	protected LocalDateTime dataOra;

	@Column
	protected double prezzo;

	@Column
	protected String tipo;


	@Column
	protected int posti;

	@ManyToOne(optional = true)
	@JoinColumn(name = "animatore_id")
	protected Animatore animatore;

	@OneToMany(
			mappedBy = "evento",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	protected Collection<TicketEvento> biglietti = new ArrayList<>();

	// ===== LOGICA POSTI =====
	public void aggiungiBiglietto(TicketEvento biglietto) {
		if (biglietto == null) return;
		if (this.posti <= 0) {
			throw new IllegalStateException("Posti esauriti");
		}
		biglietto.setEvento(this);
		this.biglietti.add(biglietto);
		this.posti = this.posti - 1;
	}

	public void rimuoviBiglietto(TicketEvento biglietto) {
		if (biglietto == null) return;
		if (this.biglietti.remove(biglietto)) {
			biglietto.setEvento(null);
			this.posti = this.posti + 1;
		}
	}

	public Long getId() { return id; }
	public String getNome() { return nome; }
	public LocalDateTime getDataOra() { return dataOra; }
	public double getPrezzo() { return prezzo; }
	public String getTipo() { return tipo; }
	public int getPosti() { return posti; }
	public Animatore getAnimatore() { return animatore; }
	public Collection<TicketEvento> getBiglietti() { return biglietti; }

	public void setId(Long id) { this.id = id; }
	public void setNome(String nome) { this.nome = nome; }
	public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
	public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
	public void setTipo(String tipo) { this.tipo = tipo; }
	public void setPosti(int posti) { this.posti = posti; }
	public void setAnimatore(Animatore animatore) { this.animatore = animatore; }
	public void setBiglietti(Collection<TicketEvento> biglietti) { this.biglietti = biglietti; }
}
