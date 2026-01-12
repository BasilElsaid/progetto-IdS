package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.CertificatoDTO;
import it.unicam.filiera.certificati.*;
import it.unicam.filiera.services.CertificatiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificati")
public class CertificatiController {

    private final CertificatiService service;

    public CertificatiController(CertificatiService service) {
        this.service = service;
    }

    @PostMapping
    public Certificato crea(@RequestBody CertificatoDTO dto) {
        return service.creaCertificato(dto);
    }

    @GetMapping
    public List<Certificato> tutti() {
        return service.getTuttiCertificati();
    }

    @GetMapping("/{id}")
    public Certificato uno(@PathVariable Long id) {
        return service.getCertificato(id);
    }

    @PutMapping("/{id}")
    public Certificato aggiorna(@PathVariable Long id, @RequestBody CertificatoDTO dto) {
        return service.aggiornaCertificato(id, dto);
    }

    @DeleteMapping("/{id}")
    public void elimina(@PathVariable Long id) {
        service.eliminaCertificato(id);
    }
}