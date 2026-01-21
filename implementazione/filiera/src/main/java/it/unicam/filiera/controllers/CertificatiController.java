package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CertificatoDTO;
import it.unicam.filiera.controllers.dto.VerificaCertificatoDTO;
import it.unicam.filiera.enums.TipoCertificatore;
import it.unicam.filiera.certificati.Certificato;
import it.unicam.filiera.services.CertificatiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificati")
@PreAuthorize("hasAnyRole('CURATORE', 'PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
public class CertificatiController {

    private final CertificatiService certificatiService;

    public CertificatiController(CertificatiService certificatiService) {
        this.certificatiService = certificatiService;
    }

    @PostMapping
    public Certificato creaCertificato(@RequestBody CertificatoDTO dto) {
        return certificatiService.creaCertificato(dto);
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'GESTORE_PIATTAFORMA')")
    @GetMapping
    public List<Certificato> getTuttiCertificati() {
        return certificatiService.getTuttiCertificati();
    }

    @GetMapping("/{id}")
    public Certificato getCertificato(@PathVariable Long id) {
        return certificatiService.getCertificato(id);
    }

    @PutMapping("/{id}")
    public Certificato aggiornaCertificato(@PathVariable Long id, @RequestBody CertificatoDTO dto) {
        return certificatiService.aggiornaCertificato(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminaCertificato(@PathVariable Long id) {
        certificatiService.eliminaCertificato(id);
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'GESTORE_PIATTAFORMA')")
    @PostMapping("/{id}/verifica")
    public boolean verificaCertificato(@PathVariable Long id, @RequestBody VerificaCertificatoDTO dto) {
        return certificatiService.verificaCertificato(id, dto.approvato, dto.commento);
    }
}