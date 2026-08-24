package datastructures.heap;

import datastructures.linear.DynamicArray;

/**
 * Integer-only MaxHeap kept for the performance experiment (heap timing benchmark).
 */
public class IntMaxHeap {

    private final DynamicArray<Integer> heap;

    public IntMaxHeap() { heap = new DynamicArray<>(); }

    public void insert(int value) {
        heap.add(value);
        bubbleUp(heap.lastIndex());
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index) <= heap.get(parent)) break;
            heap.swap(index, parent);
            index = parent;
        }
    }

    public int peek() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty.");
        return heap.get(0);
    }

    public int extractMax() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty.");
        int max = heap.get(0);
        if (heap.size() == 1) { heap.remove(0); return max; }
        heap.set(0, heap.get(heap.lastIndex()));
        heap.remove(heap.lastIndex());
        heapifyDown(0);
        return max;
    }

    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1, right = 2 * index + 2, largest = index;
            if (left  < heap.size() && heap.get(left)  > heap.get(largest)) largest = left;
            if (right < heap.size() && heap.get(right) > heap.get(largest)) largest = right;
            if (largest == index) break;
            heap.swap(index, largest);
            index = largest;
        }
    }

    public int size()        { return heap.size(); }
    public boolean isEmpty() { return heap.isEmpty(); }

    @Override
    public String toString() { return heap.toString(); }
}
