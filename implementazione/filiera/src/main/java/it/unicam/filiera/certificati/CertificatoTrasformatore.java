package it.unicam.filiera.certificati;

import it.unicam.filiera.models.Prodotto;
import jakarta.persistence.Entity;

@Entity
public class CertificatoTrasformatore extends Certificato {

	private String processo;
	private String impianto;

	@Override
	public boolean verifica(Prodotto p) {
		return true;
	}

	@Override
	public String getNome() {
		return "Certificato Trasformatore";
	}

	public String getProcesso() { return processo; }
	public void setProcesso(String processo) { this.processo = processo; }

	public String getImpianto() { return impianto; }
	public void setImpianto(String impianto) { this.impianto = impianto; }
}