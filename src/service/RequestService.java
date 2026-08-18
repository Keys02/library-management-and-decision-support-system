package service;

import datastructures.linear.ArrayQueue;
import model.ServiceRequest;

public class RequestService {

    private final ArrayQueue<ServiceRequest> requestQueue;

    public RequestService() {
        requestQueue = new ArrayQueue<>();
    }

    public void submitRequest(ServiceRequest request) {
        requestQueue.enqueue(request);
    }

    public ServiceRequest processNextRequest() {

        if (requestQueue.isEmpty()) {
            return null;
        }

        return requestQueue.dequeue();
    }

    public ServiceRequest viewNextRequest() {

        if (requestQueue.isEmpty()) {
            return null;
        }

        return requestQueue.peek();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }

    public int getPendingRequestCount() {
        return requestQueue.size();
    }

    public void clearRequests() {
        requestQueue.clear();
    }
}