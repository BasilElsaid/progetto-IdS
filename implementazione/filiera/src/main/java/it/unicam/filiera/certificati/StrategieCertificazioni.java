package it.unicam.filiera.certificati;

import it.unicam.filiera.models.Prodotto;
import it.unicam.filiera.prodotto.*;

public interface StrategieCertificazioni {

	/**
	 * 
	 * @param p
	 */
	boolean verifica(Prodotto p);

	String getNome();

}