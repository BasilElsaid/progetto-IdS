package it.unicam.filiera.dto.response;

import it.unicam.filiera.enums.AuditAction;

import java.time.LocalDateTime;

public class AuditLogResponse {

    private Long id;
    private String entityType;
    private Long entityId;
    private AuditAction action;
    private String actorUsername;
    private Long actorId;
    private LocalDateTime createdAt;
    private String details;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }

    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }

    public AuditAction getAction() { return action; }
    public void setAction(AuditAction action) { this.action = action; }

    public String getActorUsername() { return actorUsername; }
    public void setActorUsername(String actorUsername) { this.actorUsername = actorUsername; }

    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
