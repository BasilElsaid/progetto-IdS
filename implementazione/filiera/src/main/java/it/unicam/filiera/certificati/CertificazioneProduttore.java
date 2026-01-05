package it.unicam.filiera.certificati;

import it.unicam.filiera.models.Prodotto;

public class CertificazioneProduttore implements StrategieCertificazioni {

	private String azienda;
	private String origineMateriaPrima;

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