package test.linear;

import datastructures.heap.PriorityQueue;

public class PriorityQueueTest {

    public static void main(String[] args) {

        PriorityQueue queue = new PriorityQueue();

        queue.enqueue(4);
        queue.enqueue(9);
        queue.enqueue(2);
        queue.enqueue(7);
        queue.enqueue(5);

        System.out.println("Queue:");
        System.out.println(queue);

        System.out.println();

        System.out.println("Serving:");

        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}