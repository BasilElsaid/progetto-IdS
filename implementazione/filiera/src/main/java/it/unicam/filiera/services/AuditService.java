package it.unicam.filiera.services;

import it.unicam.filiera.dto.response.AuditLogResponse;
import it.unicam.filiera.domain.AuditLog;
import it.unicam.filiera.enums.AuditAction;
import it.unicam.filiera.exceptions.ForbiddenException;
import it.unicam.filiera.models.UtenteGenerico;
import it.unicam.filiera.repositories.AuditLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditRepo;

    public AuditService(AuditLogRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    public void log(String entityType, Long entityId, AuditAction action, String details) {
        UtenteGenerico user = getUtenteLoggato();
        AuditLog log = new AuditLog(entityType, entityId, action, user.getUsername(), user.getId(), details);
        auditRepo.save(log);
    }

    public List<AuditLogResponse> byEntity(String entityType, Long entityId) {
        return auditRepo.findByEntityTypeAndEntityIdOrderByCreatedAtDesc(entityType, entityId)
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }

    public List<AuditLogResponse> mine() {
        UtenteGenerico user = getUtenteLoggato();
        return auditRepo.findByActorUsernameOrderByCreatedAtDesc(user.getUsername())
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }

    private UtenteGenerico getUtenteLoggato() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UtenteGenerico user)) {
            throw new ForbiddenException("Utente non autenticato");
        }
        return user;
    }
}
