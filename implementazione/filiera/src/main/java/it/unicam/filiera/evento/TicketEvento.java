package it.unicam.filiera.evento;

import it.unicam.filiera.models.*;
import jakarta.persistence.*;

@Entity
public class TicketEvento {

	public int getNumeroTicket() {
		return numeroTicket;
	}

	public void setNumeroTicket(int numeroTicket) {
		this.numeroTicket = numeroTicket;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Acquirente getAcquirente() {
		return acquirente;
	}

	public void setAcquirente(Acquirente acquirente) {
		this.acquirente = acquirente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int numeroTicket;

	@ManyToOne(optional = false)
	@JoinColumn(name = "evento_id")
	private Evento evento;

	@ManyToOne(optional = false)
	@JoinColumn(name = "acquirente_id")
	private Acquirente acquirente;

	protected TicketEvento() {}

	public TicketEvento(Evento evento, Acquirente acquirente) {
		this.evento = evento;
		this.acquirente = acquirente;
	}
}