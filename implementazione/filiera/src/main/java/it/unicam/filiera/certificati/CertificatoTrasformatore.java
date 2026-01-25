package it.unicam.filiera.certificati;

import jakarta.persistence.Entity;

@Entity
public class CertificatoTrasformatore extends Certificato {
	private String processo;
	private String impianto;

	// getters/setters
	public String getProcesso() { return processo; }
	public void setProcesso(String processo) { this.processo = processo; }

	public String getImpianto() { return impianto; }
	public void setImpianto(String impianto) { this.impianto = impianto; }
}