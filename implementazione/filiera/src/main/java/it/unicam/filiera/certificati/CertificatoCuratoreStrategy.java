package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.repositories.CertificatiCuratoreRepository;

public class CertificatoCuratoreStrategy implements StrategieCertificazioni {

    public CertificatoCuratoreStrategy() {}

    @Override
    public Certificato creaCertificato(CreateCertificatoRequest dto, Prodotto p) {
        throw new UnsupportedOperationException("I certificati curatore si creano solo tramite verifica");
    }

    @Override
    public boolean verifica(Certificato target, Boolean approvato, String commento, CertificatiCuratoreRepository curatoreRepo) {
        if (target instanceof CertificatoCuratore) {
            throw new BadRequestException("Non è possibile verificare un altro certificato curatore");
        }

        if (curatoreRepo.existsByCertificatoTargetId(target.getId())) {
            throw new BadRequestException("Questo certificato è già stato verificato dal curatore");
        }

        CertificatoCuratore cc = new CertificatoCuratore();
        cc.setCertificatoTarget(target);
        cc.setProdotto(target.getProdotto());
        cc.setApprovato(approvato);
        cc.setCommento(commento);
        cc.setTipo(TipoCertificatore.CURATORE);

        curatoreRepo.save(cc);
        return cc.isApprovato();
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
