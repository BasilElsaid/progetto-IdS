package it.unicam.filiera.services;

import it.unicam.filiera.controllers.dto.CertificatoDTO;
import it.unicam.filiera.certificati.*;
import it.unicam.filiera.models.Prodotto;
import it.unicam.filiera.repositories.CertificatoRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CertificatiService {

    private final CertificatoRepository certificatoRepo;
    private final ProdottoRepository prodottoRepo;

    public CertificatiService(CertificatoRepository certificatoRepo, ProdottoRepository prodottoRepo) {
        this.certificatoRepo = certificatoRepo;
        this.prodottoRepo = prodottoRepo;
    }

    public Certificato creaCertificato(CertificatoDTO dto) {
        // Controllo prodotto
        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        // Validazione campi in base al tipo
        validaDto(dto);

        Certificato c;
        switch(dto.tipo) {
            case PRODUTTORE -> {
                CertificazioneProduttore cp = new CertificazioneProduttore();
                cp.setProdotto(p);
                cp.setAzienda(dto.azienda);
                cp.setOrigineMateriaPrima(dto.origineMateriaPrima);
                c = cp;
            }
            case TRASFORMATORE -> {
                CertificatoTrasformatore ct = new CertificatoTrasformatore();
                ct.setProdotto(p);
                ct.setProcesso(dto.processo);
                ct.setImpianto(dto.impianto);
                c = ct;
            }
            case CURATORE -> {
                CertificatoCuratore cc = new CertificatoCuratore();
                cc.setProdotto(p);
                cc.setApprovato(dto.approvato);
                cc.setCommento(dto.commento);
                c = cc;
            }
            default -> throw new RuntimeException("Tipo certificatore non valido");
        }

        return certificatoRepo.save(c);
    }

    // --- VALIDAZIONE "STRICT" ---
    private void validaDto(CertificatoDTO dto) {
        switch(dto.tipo) {
            case PRODUTTORE -> {
                if(dto.azienda == null || dto.origineMateriaPrima == null)
                    throw new RuntimeException("Campi azienda e origineMateriaPrima obbligatori per PRODUTTORE");
                if(dto.processo != null || dto.impianto != null || dto.approvato != null || dto.commento != null)
                    throw new RuntimeException("Campi non validi presenti per PRODUTTORE");
            }
            case TRASFORMATORE -> {
                if(dto.processo == null || dto.impianto == null)
                    throw new RuntimeException("Campi processo e impianto obbligatori per TRASFORMATORE");
                if(dto.azienda != null || dto.origineMateriaPrima != null || dto.approvato != null || dto.commento != null)
                    throw new RuntimeException("Campi non validi presenti per TRASFORMATORE");
            }
            case CURATORE -> {
                if(dto.approvato == null || dto.commento == null)
                    throw new RuntimeException("Campi approvato e commento obbligatori per CURATORE");
                if(dto.azienda != null || dto.origineMateriaPrima != null || dto.processo != null || dto.impianto != null)
                    throw new RuntimeException("Campi non validi presenti per CURATORE");
            }
        }
    }

    // --- READ ---
    public List<Certificato> getTuttiCertificati() {
        return certificatoRepo.findAll();
    }

    public Certificato getCertificato(Long id) {
        return certificatoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificato non trovato"));
    }

    // --- UPDATE ---
    public Certificato aggiornaCertificato(Long id, CertificatoDTO dto) {
        Certificato c = certificatoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificato non trovato"));

        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        c.setProdotto(p);

        validaDto(dto); // validazione anche in update

        switch(dto.tipo) {
            case PRODUTTORE -> {
                if(c instanceof CertificazioneProduttore cp) {
                    cp.setAzienda(dto.azienda);
                    cp.setOrigineMateriaPrima(dto.origineMateriaPrima);
                } else throw new RuntimeException("Tipo certificatore non corrisponde all'entità");
            }
            case TRASFORMATORE -> {
                if(c instanceof CertificatoTrasformatore ct) {
                    ct.setProcesso(dto.processo);
                    ct.setImpianto(dto.impianto);
                } else throw new RuntimeException("Tipo certificatore non corrisponde all'entità");
            }
            case CURATORE -> {
                if(c instanceof CertificatoCuratore cc) {
                    cc.setApprovato(dto.approvato);
                    cc.setCommento(dto.commento);
                } else throw new RuntimeException("Tipo certificatore non corrisponde all'entità");
            }
        }

        return certificatoRepo.save(c);
    }

    // --- DELETE ---
    public void eliminaCertificato(Long id) {
        certificatoRepo.deleteById(id);
    }
}