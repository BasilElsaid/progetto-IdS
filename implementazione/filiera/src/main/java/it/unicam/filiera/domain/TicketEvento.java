package it.unicam.filiera.domain;

import it.unicam.filiera.models.Acquirente;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class TicketEvento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private int numeroTicket;

	@ManyToOne(optional = false)
	@JoinColumn(name = "evento_id")
	private Evento evento;

	@ManyToOne(optional = false)
	@JoinColumn(name = "acquirente_id")
	private Acquirente acquirente;

	@Column(nullable = false)
	private LocalDateTime acquistatoIl;

	@Column(nullable = false)
	private boolean usato = false;

	@Column
	private LocalDateTime usatoIl;

	protected TicketEvento() {}

	public TicketEvento(Evento evento, Acquirente acquirente) {
		this.evento = evento;
		this.acquirente = acquirente;
		this.acquistatoIl = LocalDateTime.now();
		this.usato = false;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public int getNumeroTicket() { return numeroTicket; }
	public void setNumeroTicket(int numeroTicket) { this.numeroTicket = numeroTicket; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }

	public Acquirente getAcquirente() { return acquirente; }
	public void setAcquirente(Acquirente acquirente) { this.acquirente = acquirente; }

	public LocalDateTime getAcquistatoIl() { return acquistatoIl; }
	public void setAcquistatoIl(LocalDateTime acquistatoIl) { this.acquistatoIl = acquistatoIl; }

	public boolean isUsato() { return usato; }
	public void setUsato(boolean usato) { this.usato = usato; }

	public LocalDateTime getUsatoIl() { return usatoIl; }
	public void setUsatoIl(LocalDateTime usatoIl) { this.usatoIl = usatoIl; }
}
