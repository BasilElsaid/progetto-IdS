package it.unicam.filiera.certificati;

import it.unicam.filiera.enums.TipoCertificatore;
import org.springframework.stereotype.Component;

import java.util.Map;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class StrategieCertificazioniFactory {

    private final Map<TipoCertificatore, StrategieCertificazioni> strategie;

    public StrategieCertificazioniFactory() {
        strategie = Map.of(
                TipoCertificatore.PRODUTTORE, new CertificatoProduttoreStrategy(),
                TipoCertificatore.TRASFORMATORE, new CertificatoTrasformatoreStrategy(),
                TipoCertificatore.CURATORE, new CertificatoCuratoreStrategy()
        );
    }

    public StrategieCertificazioni getStrategia(TipoCertificatore tipo) {
        return strategie.get(tipo);
    }
}