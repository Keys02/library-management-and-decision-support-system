package model;

import java.time.LocalDateTime;

public class AuditEvent {
    private int id;
    private String eventType;
    private String description;
    private LocalDateTime createdAt;

    public AuditEvent(
            int id,
            String eventType,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.eventType = eventType;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}