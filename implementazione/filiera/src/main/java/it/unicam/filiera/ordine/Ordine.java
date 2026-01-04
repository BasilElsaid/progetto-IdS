package it.unicam.filiera.ordine;

import java.util.*;
import it.unicam.filiera.prodotto.*;
import it.unicam.filiera.models.*;

public class Ordine {

	private String dataOra;
	private double totale;
	private int numeroOrdine;
	private Collection<Prodotto> prodotti = new ArrayList<>();
	private Acquirente acquirente;
	private Pagamento pagamento;

	/**
	 * 
	 * @param acquirente
	 */
	public Ordine(Acquirente acquirente) {
		// TODO - implement Ordine.Ordine
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param prodotto
	 */
	public void aggiuniProdotto(Prodotto prodotto) {
		// TODO - implement Ordine.aggiuniProdotto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param pacchetto
	 */
	public void aggiuniPacchetto(Pacchetto pacchetto) {
		// TODO - implement Ordine.aggiuniPacchetto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param prodotto
	 */
	public void eliminaProdotto(Prodotto prodotto) {
		// TODO - implement Ordine.eliminaProdotto
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param pacchetto
	 */
	public void eliminaPacchetto(Pacchetto pacchetto) {
		// TODO - implement Ordine.eliminaPacchetto
		throw new UnsupportedOperationException();
	}

	public boolean paga() {
		// TODO - implement Ordine.paga
		throw new UnsupportedOperationException();
	}

}