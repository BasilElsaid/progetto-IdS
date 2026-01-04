package it.unicam.filiera.evento;

import it.unicam.filiera.models.*;
import java.util.*;

public class Evento {

	private String nome;
	private String dataOra;
	private double prezzo;
	private String tipo;
	private int posti;
	private Animatore animatore;
	private Collection<TicketEvento> biglietti = new ArrayList<>();
	private int id;

	/**
	 * 
	 * @param biglietto
	 */
	public void aggiungiBiglietto(TicketEvento biglietto) {
		// TODO - implement Evento.aggiungiBiglietto
		throw new UnsupportedOperationException();
	}

}