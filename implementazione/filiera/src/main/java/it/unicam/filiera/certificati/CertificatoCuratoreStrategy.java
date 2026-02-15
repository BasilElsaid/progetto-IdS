package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.exceptions.BadRequestException;

public class CertificatoCuratoreStrategy implements StrategieCertificazioni {

    public CertificatoCuratoreStrategy() {}

    @Override
    public Certificato creaCertificato(CreateCertificatoRequest dto, Prodotto p) {
        throw new UnsupportedOperationException("I certificati curatore si creano solo tramite verifica");
    }

    @Override
    public boolean verifica(Certificato target, boolean approvato, String commento) {
        if (target.getTipo() != TipoCertificatore.PRODUTTORE && target.getTipo() != TipoCertificatore.TRASFORMATORE) {
            throw new BadRequestException("Questo certificato non può essere verificato dal curatore");
        }

        // Aggiorna i campi direttamente sul certificato esistente
        target.setApprovato(approvato);
        target.setCommento(commento);

        return target.isApprovato();
    }

    @Override
    public void validaDto(CreateCertificatoRequest dto) {
        throw new UnsupportedOperationException("I certificati curatore si creano solo tramite verifica");
    }

    @Override
    public Certificato aggiornaCertificato(Certificato c, UpdateCertificatoRequest dto) {
        throw new UnsupportedOperationException("I certificati curatore non possono essere modificati");
    }

    @Override
    public String getNome() {
        return "Certificato Curatore";
    }
}
