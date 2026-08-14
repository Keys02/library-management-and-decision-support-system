package model;

import java.time.LocalDateTime;

public class ServiceRequest {
    private int id;
    private int patronId;
    private int bookId;
    private RequestType requestType;
    private int urgency;
    private RequestStatus status;
    private LocalDateTime createdAt;

    public ServiceRequest(
            int id,
            int patronId,
            int bookId,
            RequestType requestType,
            int urgency,
            RequestStatus status,
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

    public void updateStatus(RequestStatus status) {
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

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}