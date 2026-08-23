package service;

import datastructures.linear.ArrayQueue;
import datastructures.linear.CircularQueue;
import datastructures.heap.PriorityQueue;
import model.ServiceRequest;

/**
 * Supports three dispatch modes:
 *   FIFO            — ArrayQueue, strictly first-come-first-served
 *   CIRCULAR        — CircularQueue, round-robin rotation
 *   PRIORITY        — PriorityQueue (MaxHeap), highest urgency first
 */
public class RequestService {

    public enum DispatchMode { FIFO, CIRCULAR, PRIORITY }

    private final ArrayQueue<ServiceRequest>    fifoQueue;
    private final CircularQueue<ServiceRequest> circularQueue;
    private final PriorityQueue<ServiceRequest> priorityQueue;
    private DispatchMode mode;

    public RequestService() {
        fifoQueue     = new ArrayQueue<>();
        circularQueue = new CircularQueue<>();
        priorityQueue = new PriorityQueue<>();
        mode          = DispatchMode.FIFO;
    }

    public void setMode(DispatchMode mode) { this.mode = mode; }
    public DispatchMode getMode()          { return mode; }

    public void submitRequest(ServiceRequest request) {
        fifoQueue.enqueue(request);
        circularQueue.enqueue(request);
        priorityQueue.enqueue(request);
    }

    public ServiceRequest processNext() {
        return switch (mode) {
            case FIFO     -> fifoQueue.isEmpty()     ? null : fifoQueue.dequeue();
            case CIRCULAR -> circularQueue.isEmpty() ? null : circularQueue.dequeue();
            case PRIORITY -> priorityQueue.isEmpty() ? null : priorityQueue.dequeue();
        };
    }

    public ServiceRequest peekNext() {
        return switch (mode) {
            case FIFO     -> fifoQueue.isEmpty()     ? null : fifoQueue.peek();
            case CIRCULAR -> circularQueue.isEmpty() ? null : circularQueue.peek();
            case PRIORITY -> priorityQueue.isEmpty() ? null : priorityQueue.peek();
        };
    }

    public boolean hasPending() {
        return switch (mode) {
            case FIFO     -> !fifoQueue.isEmpty();
            case CIRCULAR -> !circularQueue.isEmpty();
            case PRIORITY -> !priorityQueue.isEmpty();
        };
    }

    public int pendingCount() {
        return switch (mode) {
            case FIFO     -> fifoQueue.size();
            case CIRCULAR -> circularQueue.size();
            case PRIORITY -> priorityQueue.size();
        };
    }
}
