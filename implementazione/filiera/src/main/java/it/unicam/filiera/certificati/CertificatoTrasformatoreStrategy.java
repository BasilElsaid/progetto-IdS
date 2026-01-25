package it.unicam.filiera.certificati;


import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.repositories.CertificatoCuratoreRepository;

public class CertificatoTrasformatoreStrategy implements StrategieCertificazioni {

    @Override
    public Certificato creaCertificato(CreateCertificatoRequest dto, Prodotto p) {
        CertificatoTrasformatore cp = new CertificatoTrasformatore();
        cp.setProdotto(p);
        cp.setImpianto(dto.impianto());
        cp.setProcesso(dto.processo());
        cp.setTipo(TipoCertificatore.TRASFORMATORE);
        return cp;
    }

    @Override
    public boolean verifica(Certificato target, Boolean approvato, String commento, CertificatoCuratoreRepository curatoreRepo) {
        return true; // non serve per trasformatore
    }

    @Override
    public void validaDto(CreateCertificatoRequest dto) {
        if (dto.processo() == null || dto.impianto() == null)
            throw new BadRequestException("Campi processo e impianto obbligatori per TRASFORMATORE");
        if (dto.azienda() != null || dto.origineMateriaPrima() != null)
            throw new BadRequestException("Campi non validi presenti per TRASFORMATORE");
    }

    @Override
    public Certificato aggiornaCertificato(Certificato c, UpdateCertificatoRequest dto) {
        if (c instanceof CertificatoTrasformatore ct) {
            if (dto.processo() != null) ct.setProcesso(dto.processo());
            if (dto.impianto() != null) ct.setImpianto(dto.impianto());
        }
        return c;
    }

    @Override
    public String getNome() {
        return "Certificato Trasformatore";
    }
}