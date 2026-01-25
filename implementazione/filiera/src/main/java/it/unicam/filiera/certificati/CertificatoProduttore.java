package it.unicam.filiera.certificati;

import jakarta.persistence.Entity;

@Entity
public class CertificatoProduttore extends Certificato {
	private String azienda;
	private String origineMateriaPrima;

	public String getAzienda() { return azienda; }
	public void setAzienda(String azienda) { this.azienda = azienda; }

	public String getOrigineMateriaPrima() { return origineMateriaPrima; }
	public void setOrigineMateriaPrima(String origineMateriaPrima) { this.origineMateriaPrima = origineMateriaPrima; }
}