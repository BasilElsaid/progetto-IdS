package it.unicam.filiera.certificati;

import it.unicam.filiera.prodotto.Prodotto;

public class CertificatoTrasformatore implements StrategieCertificazioni {

	private String processo;
	private String impianto;

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