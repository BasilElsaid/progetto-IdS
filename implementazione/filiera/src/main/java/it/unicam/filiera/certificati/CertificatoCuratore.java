package it.unicam.filiera.certificati;

import it.unicam.filiera.models.Prodotto;
import jakarta.persistence.Entity;

@Entity
public class CertificatoCuratore extends Certificato {

	private boolean approvato;
	private String commento;

	@Override
	public boolean verifica(Prodotto p) {
		return approvato;
	}

	@Override
	public String getNome() {
		return "Certificato Curatore";
	}

	public boolean isApprovato() { return approvato; }
	public void setApprovato(boolean approvato) { this.approvato = approvato; }

	public String getCommento() { return commento; }
	public void setCommento(String commento) { this.commento = commento; }
}