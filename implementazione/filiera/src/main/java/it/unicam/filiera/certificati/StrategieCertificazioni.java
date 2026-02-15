package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;

public interface StrategieCertificazioni {

	Certificato creaCertificato(CreateCertificatoRequest dto, Prodotto p);
	boolean verifica(Certificato target, boolean approvato, String commento);
	void validaDto(CreateCertificatoRequest dto);
	Certificato aggiornaCertificato(Certificato c, UpdateCertificatoRequest dto);  // nuovo
	String getNome();
}