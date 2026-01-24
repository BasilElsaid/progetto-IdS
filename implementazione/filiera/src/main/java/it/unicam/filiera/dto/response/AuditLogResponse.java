package it.unicam.filiera.dto.response;

import it.unicam.filiera.enums.AuditAction;
import it.unicam.filiera.domain.AuditLog;
import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        String entityType,
        Long entityId,
        AuditAction action,
        String actorUsername,
        Long actorId,
        LocalDateTime createdAt,
        String details
) {
    public static AuditLogResponse from(AuditLog a) {
        return new AuditLogResponse(
                a.getId(),
                a.getEntityType(),
                a.getEntityId(),
                a.getAction(),
                a.getActorUsername(),
                a.getActorId(),
                a.getCreatedAt(),
                a.getDetails()
        );
    }
}