package test.linear;

import datastructures.heap.MaxHeap;

public class HeapTest {

    public static void main(String[] args) {

        MaxHeap heap = new MaxHeap();

        heap.insert(40);
        heap.insert(70);
        heap.insert(50);
        heap.insert(90);
        heap.insert(30);

        System.out.println("Heap:");
        System.out.println(heap);

        System.out.println();

        System.out.println("Extracting Max:");

        while (!heap.isEmpty()) {

            System.out.println(heap.extractMax());

        }
    }
}