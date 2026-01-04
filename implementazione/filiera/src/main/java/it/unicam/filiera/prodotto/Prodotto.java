package it.unicam.filiera.prodotto;

import it.unicam.filiera.models.*;

public class Prodotto {

	private String nome;
	private String descrizione;
	private Azienda azienda;
	private Certificazione certificazione;
	private int id;

	public Azienda getAzienda() {
		return this.azienda;
	}

	public void setAzienda(Azienda azienda) {
		this.azienda = azienda;
	}

	/**
	 * 
	 * @param nome
	 * @param descrizione
	 */
	public Prodotto(String nome, String descrizione) {
		// TODO - implement Prodotto.Prodotto
		throw new UnsupportedOperationException();
	}

}