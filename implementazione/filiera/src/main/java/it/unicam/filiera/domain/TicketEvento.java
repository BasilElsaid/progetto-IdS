package it.unicam.filiera.domain;

import it.unicam.filiera.models.Acquirente;
import it.unicam.filiera.models.UtenteGenerico;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TicketEvento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private int numeroTicket;

	@Column(unique = true)
	private String qrCode;

	@ManyToOne(optional = false)
	@JoinColumn(name = "evento_id")
	private Evento evento;

	@ManyToOne(optional = false)
	@JoinColumn(name = "proprietario_id")
	private UtenteGenerico proprietario;

	@Column(nullable = false)
	private LocalDateTime acquistatoIl;

	@Column(nullable = false)
	private boolean usato = false;

	@Column
	private LocalDateTime usatoIl;

	@ManyToOne
	@JoinColumn(name = "checkin_operatore_id")
	private UtenteGenerico checkInDa;

	protected TicketEvento() {}

	public TicketEvento(Evento evento, UtenteGenerico proprietario) {
		this.evento = evento;
		this.proprietario = proprietario;
		this.acquistatoIl = LocalDateTime.now();
		this.usato = false;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public int getNumeroTicket() { return numeroTicket; }
	public void setNumeroTicket(int numeroTicket) { this.numeroTicket = numeroTicket; }

	public String getQrCode() { return qrCode; }
	public void setQrCode(String qrCode) { this.qrCode = qrCode; }

	public Evento getEvento() { return evento; }
	public void setEvento(Evento evento) { this.evento = evento; }

	public UtenteGenerico getProprietario() { return proprietario; }
	public void setProprietario(UtenteGenerico proprietario) { this.proprietario = proprietario; }

	public LocalDateTime getAcquistatoIl() { return acquistatoIl; }
	public void setAcquistatoIl(LocalDateTime acquistatoIl) { this.acquistatoIl = acquistatoIl; }

	public boolean isUsato() { return usato; }
	public void setUsato(boolean usato) { this.usato = usato; }

	public LocalDateTime getUsatoIl() { return usatoIl; }
	public void setUsatoIl(LocalDateTime usatoIl) { this.usatoIl = usatoIl; }

	public UtenteGenerico getCheckInDa() { return checkInDa; }
	public void setCheckInDa(UtenteGenerico checkInDa) { this.checkInDa = checkInDa; }
}
