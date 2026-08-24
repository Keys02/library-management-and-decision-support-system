package test.linear;

import datastructures.heap.PriorityQueue;
import model.RequestStatus;
import model.RequestType;
import model.ServiceRequest;

import java.time.LocalDateTime;

public class PriorityQueueTest {

    public static void main(String[] args) {

        PriorityQueue<ServiceRequest> queue = new PriorityQueue<>();

        queue.enqueue(new ServiceRequest(
                1, 101, 201,
                RequestType.BORROW,
                4,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        queue.enqueue(new ServiceRequest(
                2, 102, 202,
                RequestType.RETURN,
                9,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        queue.enqueue(new ServiceRequest(
                3, 103, 203,
                RequestType.RESERVE,
                2,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        queue.enqueue(new ServiceRequest(
                4, 104, 204,
                RequestType.BORROW,
                7,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        System.out.println("Queue:");
        System.out.println(queue);

        System.out.println();
        System.out.println("Serving:");

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}