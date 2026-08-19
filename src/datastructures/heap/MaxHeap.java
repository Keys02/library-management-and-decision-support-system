package datastructures.heap;

import datastructures.linear.DynamicArray;

public class MaxHeap {

    private final DynamicArray<Integer> heap;

    public MaxHeap() {
        heap = new DynamicArray<>();
    }

    public void insert(int value) {

        heap.add(value);

        bubbleUp(heap.lastIndex());
    }

    private void bubbleUp(int index) {

        while (index > 0) {

            int parent = (index - 1) / 2;

            if (heap.get(index) <= heap.get(parent)) {
                break;
            }

            heap.swap(index, parent);

            index = parent;
        }
    }

    public int peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty.");
        }

        return heap.get(0);
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}