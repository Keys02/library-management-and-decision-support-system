package model;

import java.time.LocalDateTime;

public class ServiceRequest {
    private int id;
    private int patronId;
    private int bookId;
    private String requestType;
    private int urgency;
    private String status;
    private LocalDateTime createdAt;

    public ServiceRequest(
            int id,
            int patronId,
            int bookId,
            String requestType,
            int urgency,
            String status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.patronId = patronId;
        this.bookId = bookId;
        this.requestType = requestType;
        this.urgency = urgency;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int calculatePriority() {
        return urgency;
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
