package it.unicam.filiera.models;

import java.util.*;
import it.unicam.filiera.ordine.*;
import it.unicam.filiera.evento.*;

public class Acquirente extends UtenteGenerico {

	private String username;
	private String password;
	private String nome;
	private String cognome;
	private String email;
	private String telefono;
	private Collection<Ordine> ordini = new ArrayList<>();
	private Collection<TicketEvento> biglietti = new ArrayList<>();
	private int id;

	/**
	 * 
	 * @param ordine
	 */
	public void aggiungiOrdine(Ordine ordine) {
		// TODO - implement Acquirente.aggiungiOrdine
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param biglietto
	 */
	public void aggiungiBiglietto(TicketEvento biglietto) {
		// TODO - implement Acquirente.aggiungiBiglietto
		throw new UnsupportedOperationException();
	}

}