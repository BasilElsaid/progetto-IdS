package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.repositories.CertificatoCuratoreRepository;

public interface StrategieCertificazioni {

	Certificato creaCertificato(CreateCertificatoRequest dto, Prodotto p);
	boolean verifica(Certificato target, Boolean approvato, String commento, CertificatoCuratoreRepository curatoreRepo);
	void validaDto(CreateCertificatoRequest dto);
	Certificato aggiornaCertificato(Certificato c, UpdateCertificatoRequest dto);  // nuovo
	String getNome();
}