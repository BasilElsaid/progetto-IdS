package it.unicam.filiera.models;

import java.util.*;
import it.unicam.filiera.evento.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Animatore extends Personale {

	@Id
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "animatore", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Evento> eventi = new ArrayList<>();

	/**
	 * 
	 * @param evento
	 */
	public void aggiungiEvento(Evento evento) {
		// TODO - implement Animatore.aggiungiEvento
		throw new UnsupportedOperationException();
	}

}