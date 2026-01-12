package it.unicam.filiera.certificati;

import it.unicam.filiera.models.TipoCertificatore;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StrategieCertificazioniFactory {

    private final Map<TipoCertificatore, StrategieCertificazioni> strategie;

    public StrategieCertificazioniFactory() {
        strategie = Map.of(
                TipoCertificatore.PRODUTTORE, (StrategieCertificazioni) new CertificazioneProduttore(),
                TipoCertificatore.TRASFORMATORE, (StrategieCertificazioni) new CertificatoTrasformatore(),
                TipoCertificatore.CURATORE, (StrategieCertificazioni) new CertificatoCuratore()
        );
    }

    public StrategieCertificazioni getStrategia(TipoCertificatore tipo) {
        return strategie.get(tipo);
    }
}

