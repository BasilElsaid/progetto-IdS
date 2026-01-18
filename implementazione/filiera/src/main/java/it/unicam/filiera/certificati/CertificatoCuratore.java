package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import jakarta.persistence.Entity;

@Entity
public class CertificatoCuratore extends Certificato implements StrategieCertificazioni {

	private boolean approvato; // default false
	private String commento;

	@Override
	public boolean verifica(Prodotto p) {
		// ritorna true solo se approvato è true
		return approvato;
	}

	@Override
	public String getNome() {
		return "Certificato Curatore";
	}

	// getter/setter
	public boolean isApprovato() { return approvato; }
	public void setApprovato(boolean approvato) { this.approvato = approvato; }

	public String getCommento() { return commento; }
	public void setCommento(String commento) { this.commento = commento; }
}