package test.linear;

import datastructures.heap.MaxHeap;
import model.RequestStatus;
import model.RequestType;
import model.ServiceRequest;

import java.time.LocalDateTime;

public class HeapTest {

    public static void main(String[] args) {

        MaxHeap<ServiceRequest> heap = new MaxHeap<>();

        heap.insert(new ServiceRequest(
                1, 101, 201,
                RequestType.BORROW,
                4,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        heap.insert(new ServiceRequest(
                2, 102, 202,
                RequestType.RETURN,
                9,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        heap.insert(new ServiceRequest(
                3, 103, 203,
                RequestType.RESERVE,
                5,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        heap.insert(new ServiceRequest(
                4, 104, 204,
                RequestType.BORROW,
                7,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        heap.insert(new ServiceRequest(
                5, 105, 205,
                RequestType.RETURN,
                3,
                RequestStatus.PENDING,
                LocalDateTime.now()
        ));

        System.out.println("Heap:");
        System.out.println(heap);

        System.out.println();

        System.out.println("Extracting Max:");

        while (!heap.isEmpty()) {
            System.out.println(heap.extractMax());
        }
    }
}