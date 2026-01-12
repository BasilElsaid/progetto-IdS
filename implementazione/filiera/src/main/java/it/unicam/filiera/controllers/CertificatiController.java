package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CertificatoDTO;
import it.unicam.filiera.models.TipoCertificatore;
import it.unicam.filiera.certificati.Certificato;
import it.unicam.filiera.services.CertificatiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificati")
public class CertificatiController {

    private final CertificatiService certificatiService;

    public CertificatiController(CertificatiService certificatiService) {
        this.certificatiService = certificatiService;
    }

    // --- CREATE ---
    @PostMapping
    public Certificato creaCertificato(@RequestBody CertificatoDTO dto) {
        return certificatiService.creaCertificato(dto);
    }

    // --- READ ---
    @GetMapping
    public List<Certificato> getTuttiCertificati() {
        return certificatiService.getTuttiCertificati();
    }

    @GetMapping("/{id}")
    public Certificato getCertificato(@PathVariable Long id) {
        return certificatiService.getCertificato(id);
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public Certificato aggiornaCertificato(@PathVariable Long id, @RequestBody CertificatoDTO dto) {
        return certificatiService.aggiornaCertificato(id, dto);
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public void eliminaCertificato(@PathVariable Long id) {
        certificatiService.eliminaCertificato(id);
    }

    // --- VERIFICA CERTIFICATO (opzionale) ---
    @PostMapping("/verifica")
    public boolean verificaCertificato(@RequestParam TipoCertificatore tipo,
                                       @RequestParam Long prodottoId) {
        return certificatiService.verificaCertificato(tipo, prodottoId);
    }
}