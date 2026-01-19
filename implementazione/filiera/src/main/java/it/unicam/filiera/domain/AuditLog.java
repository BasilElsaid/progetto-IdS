package it.unicam.filiera.domain;

import it.unicam.filiera.enums.AuditAction;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entityType;

    @Column(nullable = false)
    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column(nullable = false)
    private String actorUsername;

    private Long actorId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 4000)
    private String details;

    protected AuditLog() {}

    public AuditLog(String entityType, Long entityId, AuditAction action, String actorUsername, Long actorId, String details) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.actorUsername = actorUsername;
        this.actorId = actorId;
        this.details = details;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getEntityType() { return entityType; }
    public Long getEntityId() { return entityId; }
    public AuditAction getAction() { return action; }
    public String getActorUsername() { return actorUsername; }
    public Long getActorId() { return actorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getDetails() { return details; }

    public void setId(Long id) { this.id = id; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public void setAction(AuditAction action) { this.action = action; }
    public void setActorUsername(String actorUsername) { this.actorUsername = actorUsername; }
    public void setActorId(Long actorId) { this.actorId = actorId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setDetails(String details) { this.details = details; }
}
