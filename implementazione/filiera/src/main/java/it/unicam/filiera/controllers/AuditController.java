package it.unicam.filiera.controllers;

import it.unicam.filiera.controllers.dto.response.AuditLogResponse;
import it.unicam.filiera.services.AuditService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasAnyRole('GESTORE_PIATTAFORMA')")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditLogResponse> byEntity(@RequestParam String entity, @RequestParam Long id) {
        return auditService.byEntity(entity, id);
    }

    @GetMapping("/mine")
    public List<AuditLogResponse> mine() {
        return auditService.mine();
    }
}
