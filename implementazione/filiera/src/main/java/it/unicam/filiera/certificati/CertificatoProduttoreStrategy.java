package it.unicam.filiera.certificati;

import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.exceptions.BadRequestException;

public class CertificatoProduttoreStrategy implements StrategieCertificazioni {

    @Override
    public Certificato creaCertificato(CreateCertificatoRequest dto, Prodotto p) {
        CertificatoProduttore cp = new CertificatoProduttore();
        cp.setProdotto(p);
        cp.setAzienda(dto.azienda());
        cp.setOrigineMateriaPrima(dto.origineMateriaPrima());
        cp.setTipo(TipoCertificatore.PRODUTTORE);
        return cp;
    }

    @Override
    public boolean verifica(Certificato target, boolean approvato, String commento) {
        return true; // non serve per produttore
    }

    @Override
    public void validaDto(CreateCertificatoRequest dto) {
        if (dto.azienda() == null || dto.origineMateriaPrima() == null)
            throw new BadRequestException("Campi azienda e origineMateriaPrima obbligatori per PRODUTTORE");
        if (dto.processo() != null || dto.impianto() != null)
            throw new BadRequestException("Campi non validi presenti per PRODUTTORE");
    }

    @Override
    public Certificato aggiornaCertificato(Certificato c, UpdateCertificatoRequest dto) {
        if (c instanceof CertificatoProduttore cp) {
            if (dto.azienda() != null) cp.setAzienda(dto.azienda());
            if (dto.origineMateriaPrima() != null) cp.setOrigineMateriaPrima(dto.origineMateriaPrima());
        }
        return c;
    }

    @Override
    public String getNome() {
        return "Certificazione Produttore";
    }
}