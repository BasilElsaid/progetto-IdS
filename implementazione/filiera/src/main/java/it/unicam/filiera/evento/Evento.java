package it.unicam.filiera.evento;

import it.unicam.filiera.models.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

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

	@ManyToOne(optional = true) // ora può essere null
	@JoinColumn(name = "animatore_id") // rimosso unique
	protected Animatore animatore;

	@OneToMany(
			mappedBy = "evento",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	protected Collection<TicketEvento> biglietti = new ArrayList<>();

	/**
	 * 
	 * @param biglietto
	 */
	public void aggiungiBiglietto(TicketEvento biglietto) {
		// TODO - implement Evento.aggiungiBiglietto
		throw new UnsupportedOperationException();
	}

	public Long getId() { return id; }
	public String getNome() { return nome; }
	public LocalDateTime getDataOra() { return dataOra; }
	public double getPrezzo() { return prezzo; }
	public String getTipo() { return tipo;}
	public int getPosti() { return posti; }
	public Animatore getAnimatore() { return animatore; }


	public void setId(Long id) { this.id = id; }
	public void setNome(String nome) { this.nome = nome; }
	public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }
	public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
	public void setTipo(String tipo) { this.tipo = tipo; }
	public void setPosti(int posti) { this.posti = posti; }
	public void setAnimatore(Animatore animatore) { this.animatore = animatore; }

}