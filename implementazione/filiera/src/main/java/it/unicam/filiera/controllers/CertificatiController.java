package it.unicam.filiera.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import it.unicam.filiera.dto.create.CreateCertificatoRequest;
import it.unicam.filiera.dto.create.CreateVerificaCertificatoRequest;
import it.unicam.filiera.certificati.Certificato;
import it.unicam.filiera.dto.update.UpdateCertificatoRequest;
import it.unicam.filiera.services.CertificatiService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificati")
@Tag(name = "08 - Certificati", description = "Gestione certificati")
@PreAuthorize("hasAnyRole('CURATORE', 'PRODUTTORE', 'TRASFORMATORE', 'GESTORE_PIATTAFORMA')")
public class CertificatiController {

    private final CertificatiService certificatiService;

    public CertificatiController(CertificatiService certificatiService) {
        this.certificatiService = certificatiService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUTTORE','TRASFORMATORE')")
    public List<Certificato> creaCertificati(@RequestBody List<CreateCertificatoRequest> dtos) {
        return dtos.stream()
                .map(certificatiService::creaCertificato)
                .toList();
    }

    @PreAuthorize("hasAnyRole('CURATORE', 'GESTORE_PIATTAFORMA')")
    @PostMapping("/{id}/verifica")
    public boolean verificaCertificato(@PathVariable Long id,
                                       @RequestBody CreateVerificaCertificatoRequest dto) {
        return certificatiService.verificaCertificato(id, dto.approvato(), dto.commento());
    }

    @GetMapping
    public List<Certificato> getTuttiCertificati() {
        return certificatiService.getTuttiCertificati();
    }

    @PreAuthorize("hasAnyRole('PRODUTTORE', 'TRASFORMATORE')")
    @PatchMapping("/{id}")
    public Certificato patchCertificato(@PathVariable Long id,
                                        @RequestBody UpdateCertificatoRequest dto) {
        return certificatiService.patchCertificato(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminaCertificato(@PathVariable Long id) {
        certificatiService.eliminaCertificato(id);
    }

}