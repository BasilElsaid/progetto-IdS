package it.unicam.filiera.services;


import it.unicam.filiera.controllers.dto.CertificatoDTO;
import it.unicam.filiera.certificati.*;
import it.unicam.filiera.exceptions.BadRequestException;
import it.unicam.filiera.exceptions.NotFoundException;
import it.unicam.filiera.domain.Prodotto;
import it.unicam.filiera.enums.TipoCertificatore;
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
    private final StrategieCertificazioniFactory strategieFactory;

    public CertificatiService(CertificatoRepository certificatoRepo,
                              ProdottoRepository prodottoRepo,
                              StrategieCertificazioniFactory strategieFactory) {
        this.certificatoRepo = certificatoRepo;
        this.prodottoRepo = prodottoRepo;
        this.strategieFactory = strategieFactory;
    }

    // --- CREATE ---
    public Certificato creaCertificato(CertificatoDTO dto) {
        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

        // Validazione strict dei campi
        validaDto(dto);

        // Creo certificato concreto basato sul tipo
        Certificato c;
        switch(dto.tipo) {
            case PRODUTTORE -> {
                CertificazioneProduttore cp = new CertificazioneProduttore();
                cp.setProdotto(p);
                cp.setAzienda(dto.azienda);
                cp.setOrigineMateriaPrima(dto.origineMateriaPrima);
                cp.setTipo(TipoCertificatore.PRODUTTORE);
                c = cp;
            }
            case TRASFORMATORE -> {
                CertificatoTrasformatore ct = new CertificatoTrasformatore();
                ct.setProdotto(p);
                ct.setProcesso(dto.processo);
                ct.setImpianto(dto.impianto);
                ct.setTipo(TipoCertificatore.TRASFORMATORE);
                c = ct;
            }
            case CURATORE -> {
                CertificatoCuratore cc = new CertificatoCuratore();
                cc.setProdotto(p);
                cc.setApprovato(dto.approvato);
                cc.setCommento(dto.commento);
                cc.setTipo(TipoCertificatore.CURATORE);
                c = cc;
            }
            default -> throw new BadRequestException("Tipo certificatore non valido");
        }

        return certificatoRepo.save(c);
    }

    // --- Recupero Prodotto ---
    public Prodotto getProdotto(Long prodottoId) {
        return prodottoRepo.findById(prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato con id: " + prodottoId));
    }

    // --- READ ---
    public List<Certificato> getTuttiCertificati() {
        return certificatoRepo.findAll();
    }

    public Certificato getCertificato(Long id) {
        return certificatoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Certificato non trovato"));
    }

    // --- UPDATE ---
    public Certificato aggiornaCertificato(Long id, CertificatoDTO dto) {
        Certificato c = certificatoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Certificato non trovato"));

        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new NotFoundException("Prodotto non trovato"));

        c.setProdotto(p);

        // Validazione strict
        validaDto(dto);

        switch(dto.tipo) {
            case PRODUTTORE -> {
                if(c instanceof CertificazioneProduttore cp) {
                    cp.setAzienda(dto.azienda);
                    cp.setOrigineMateriaPrima(dto.origineMateriaPrima);
                } else throw new BadRequestException("Tipo certificatore non corrisponde all'entità");
            }
            case TRASFORMATORE -> {
                if(c instanceof CertificatoTrasformatore ct) {
                    ct.setProcesso(dto.processo);
                    ct.setImpianto(dto.impianto);
                } else throw new BadRequestException("Tipo certificatore non corrisponde all'entità");
            }
            case CURATORE -> {
                if(c instanceof CertificatoCuratore cc) {
                    cc.setApprovato(dto.approvato);
                    cc.setCommento(dto.commento);
                } else throw new BadRequestException("Tipo certificatore non corrisponde all'entità");
            }
        }

        // Aggiorno tipo anche in caso di update
        c.setTipo(dto.tipo);

        return certificatoRepo.save(c);
    }

    // --- DELETE ---
    public void eliminaCertificato(Long id) {
        if (!certificatoRepo.existsById(id)) {
            throw new NotFoundException("Certificato non trovato");
        }
        certificatoRepo.deleteById(id);
    }

    // --- VERIFICA STRATEGY ---
    public boolean verificaCertificato(TipoCertificatore tipo, Long prodottoId) {
        List<Certificato> certificati = certificatoRepo.findByProdottoIdAndTipo(prodottoId, tipo);
        if(certificati.isEmpty()) return false; // Nessun certificato -> false

        // Prendi il certificato salvato (ad esempio il primo)
        Certificato c = certificati.get(0);

        // Applica la verifica sul certificato effettivo
        if(c instanceof StrategieCertificazioni sc) {
            return sc.verifica(c.getProdotto());
        }

        return false;
    }

    // --- VALIDAZIONE STRICT ---
    private void validaDto(CertificatoDTO dto) {
        switch(dto.tipo) {
            case PRODUTTORE -> {
                if(dto.azienda == null || dto.origineMateriaPrima == null)
                    throw new BadRequestException("Campi azienda e origineMateriaPrima obbligatori per PRODUTTORE");
                if(dto.processo != null || dto.impianto != null || dto.approvato != null || dto.commento != null)
                    throw new BadRequestException("Campi non validi presenti per PRODUTTORE");
            }
            case TRASFORMATORE -> {
                if(dto.processo == null || dto.impianto == null)
                    throw new BadRequestException("Campi processo e impianto obbligatori per TRASFORMATORE");
                if(dto.azienda != null || dto.origineMateriaPrima != null || dto.approvato != null || dto.commento != null)
                    throw new BadRequestException("Campi non validi presenti per TRASFORMATORE");
            }
            case CURATORE -> {
                if(dto.approvato == null || dto.commento == null)
                    throw new BadRequestException("Campi approvato e commento obbligatori per CURATORE");
                if(dto.azienda != null || dto.origineMateriaPrima != null || dto.processo != null || dto.impianto != null)
                    throw new BadRequestException("Campi non validi presenti per CURATORE");
            }
        }
    }
}