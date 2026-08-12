package model;

import java.util.ArrayList;
import java.util.List;

public class Patron {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;

    private final List<ServiceRequest> serviceRequests;

    public Patron(
            int id,
            String name,
            String email,
            String phoneNumber
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;

        this.serviceRequests = new ArrayList<>();
    }

    public List<ServiceRequest> getActiveRequests() {
        return serviceRequests.stream()
                .filter(request -> request.getStatus().equals("PENDING")
                        || request.getStatus().equals("PROCESSING"))
                .toList();
    }

    public String getDetails() {
        return String.format(
                "Patron{id=%d, name='%s', email='%s', phoneNumber='%s'}",
                id,
                name,
                email,
                phoneNumber
        );
    }

    public void addServiceRequest(ServiceRequest request) {
        serviceRequests.add(request);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
