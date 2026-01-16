package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;

public interface StrategieCertificazioni {

	/**
	 * 
	 * @param p
	 */
	boolean verifica(Prodotto p);

	String getNome();

}