package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CertificatoCuratore extends Certificato implements StrategieCertificazioni {

	private boolean approvato; // default false
	private String commento;

	@ManyToOne(optional = false)
	@JoinColumn(name = "certificato_target_id")
	private Certificato certificatoTarget;

	@Override
	public boolean verifica(Prodotto p) {
		return approvato && (certificatoTarget instanceof CertificazioneProduttore
				|| certificatoTarget instanceof CertificatoTrasformatore);
	}

	@Override
	public String getNome() {
		return "Certificato Curatore";
	}

	public boolean isApprovato() { return approvato; }
	public void setApprovato(boolean approvato) { this.approvato = approvato; }

	public String getCommento() { return commento; }
	public void setCommento(String commento) { this.commento = commento; }

	public Certificato getCertificatoTarget() { return certificatoTarget; }
	public void setCertificatoTarget(Certificato certificatoTarget) {
		this.certificatoTarget = certificatoTarget;
	}
}