package it.unicam.filiera.certificati;

import it.unicam.filiera.models.Prodotto;
import jakarta.persistence.Entity;

@Entity
public class CertificazioneProduttore extends Certificato
		implements StrategieCertificazioni{

	private String azienda;
	private String origineMateriaPrima;

	@Override
	public boolean verifica(Prodotto p) {
		// logica reale qui
		return true;
	}

	@Override
	public String getNome() {
		return "Certificazione Produttore";
	}

	public String getAzienda() { return azienda; }
	public void setAzienda(String azienda) { this.azienda = azienda; }

	public String getOrigineMateriaPrima() { return origineMateriaPrima; }
	public void setOrigineMateriaPrima(String origine) { this.origineMateriaPrima = origine; }
}