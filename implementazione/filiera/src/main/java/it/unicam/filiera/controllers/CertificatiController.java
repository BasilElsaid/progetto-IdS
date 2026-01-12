package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CertificatoDTO;
import it.unicam.filiera.certificati.*;
import it.unicam.filiera.models.Prodotto;
import it.unicam.filiera.models.TipoCertificatore;
import it.unicam.filiera.repositories.CertificatoRepository;
import it.unicam.filiera.repositories.ProdottoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificati")
public class CertificatiController {

    private final CertificatoRepository certificatoRepo;
    private final ProdottoRepository prodottoRepo;

    public CertificatiController(CertificatoRepository certificatoRepo, ProdottoRepository prodottoRepo) {
        this.certificatoRepo = certificatoRepo;
        this.prodottoRepo = prodottoRepo;
    }

    // --- CREATE ---
    @PostMapping
    public Certificato creaCertificato(@RequestBody CertificatoDTO dto) {
        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

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

    // --- READ ---
    @GetMapping
    public List<Certificato> getTuttiCertificati() {
        return certificatoRepo.findAll();
    }

    @GetMapping("/{id}")
    public Certificato getCertificato(@PathVariable Long id) {
        return certificatoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificato non trovato"));
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public Certificato aggiornaCertificato(@PathVariable Long id, @RequestBody CertificatoDTO dto) {
        Certificato c = certificatoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificato non trovato"));

        Prodotto p = prodottoRepo.findById(dto.prodottoId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        c.setProdotto(p);

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
    @DeleteMapping("/{id}")
    public void eliminaCertificato(@PathVariable Long id) {
        certificatoRepo.deleteById(id);
    }
}