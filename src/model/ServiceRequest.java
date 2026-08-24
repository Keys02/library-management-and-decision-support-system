package model;

import datastructures.interfaces.PriorityItem;
import java.time.LocalDateTime;

public class ServiceRequest implements PriorityItem {
    private int id;
    private int patronId;
    private int bookId;
    private RequestType requestType;
    private int urgency;
    private RequestStatus status;
    private LocalDateTime createdAt;

    public ServiceRequest(int id, int patronId, int bookId, RequestType requestType,
                          int urgency, RequestStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.patronId = patronId;
        this.bookId = bookId;
        this.requestType = requestType;
        this.urgency = urgency;
        this.status = status;
        this.createdAt = createdAt;
    }

    /** Required by PriorityItem — heap orders by this value, highest first. */
    @Override
    public int getPriority() { return urgency; }

    public int calculatePriority() { return urgency; }
    public void updateStatus(RequestStatus status) { this.status = status; }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getPatronId()                    { return patronId; }
    public void setPatronId(int p)              { this.patronId = p; }
    public int getBookId()                      { return bookId; }
    public void setBookId(int b)                { this.bookId = b; }
    public RequestType getRequestType()         { return requestType; }
    public void setRequestType(RequestType t)   { this.requestType = t; }
    public int getUrgency()                     { return urgency; }
    public void setUrgency(int u)               { this.urgency = u; }
    public RequestStatus getStatus()            { return status; }
    public void setStatus(RequestStatus s)      { this.status = s; }
    public LocalDateTime getCreatedAt()         { return createdAt; }
    public void setCreatedAt(LocalDateTime d)   { this.createdAt = d; }

    @Override
    public String toString() {
        return String.format("Request{id=%d, patron=%d, book=%d, type=%s, urgency=%d, status=%s}",
                id, patronId, bookId, requestType, urgency, status);
    }
}
