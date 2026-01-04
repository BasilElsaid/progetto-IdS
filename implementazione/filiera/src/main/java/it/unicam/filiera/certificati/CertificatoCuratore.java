package it.unicam.filiera.certificati;

import it.unicam.filiera.prodotto.Prodotto;

public class CertificatoCuratore implements StrategieCertificazioni {

	private boolean approvato;
	private String commento;

	/**
	 * @param p
	 */
	@Override
	public boolean verifica(Prodotto p) {
		return false;
	}

	@Override
	public String getNome() {
		return "";
	}
}