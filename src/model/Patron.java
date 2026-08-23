package model;

import datastructures.linear.LinkedList;

public class Patron {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private final LinkedList<ServiceRequest> serviceRequests;

    public Patron(int id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.serviceRequests = new LinkedList<>();
    }

    public void addServiceRequest(ServiceRequest request) { serviceRequests.addLast(request); }

    public LinkedList<ServiceRequest> getActiveRequests() {
        LinkedList<ServiceRequest> active = new LinkedList<>();
        for (int i = 0; i < serviceRequests.size(); i++) {
            ServiceRequest r = serviceRequests.get(i);
            if (r.getStatus() == RequestStatus.PENDING || r.getStatus() == RequestStatus.PROCESSING) {
                active.addLast(r);
            }
        }
        return active;
    }

    public String getDetails() {
        return String.format("Patron{id=%d, name='%s', email='%s', phoneNumber='%s'}", id, name, email, phoneNumber);
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }
    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }
    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }
    public String getPhoneNumber()       { return phoneNumber; }
    public void setPhoneNumber(String p) { this.phoneNumber = p; }
}
