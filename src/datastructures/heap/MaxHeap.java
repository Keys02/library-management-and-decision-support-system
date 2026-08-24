package datastructures.heap;

import datastructures.interfaces.PriorityItem;
import datastructures.linear.DynamicArray;

/**
 * Generic MaxHeap — stores any PriorityItem, ordered by getPriority() descending.
 * Used by the priority-based service-request dispatcher.
 */
public class MaxHeap<T extends PriorityItem> {

    private final DynamicArray<T> heap;

    public MaxHeap() {
        heap = new DynamicArray<>();
    }

    public void insert(T item) {
        heap.add(item);
        bubbleUp(heap.lastIndex());
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).getPriority() <= heap.get(parent).getPriority()) break;
            heap.swap(index, parent);
            index = parent;
        }
    }

    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty.");
        return heap.get(0);
    }

    public T extractMax() {
        if (isEmpty()) throw new IllegalStateException("Heap is empty.");
        T max = heap.get(0);
        if (heap.size() == 1) { heap.remove(0); return max; }
        heap.set(0, heap.get(heap.lastIndex()));
        heap.remove(heap.lastIndex());
        heapifyDown(0);
        return max;
    }

    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;
            if (left  < heap.size() && heap.get(left).getPriority()  > heap.get(largest).getPriority()) largest = left;
            if (right < heap.size() && heap.get(right).getPriority() > heap.get(largest).getPriority()) largest = right;
            if (largest == index) break;
            heap.swap(index, largest);
            index = largest;
        }
    }

    public int size()       { return heap.size(); }
    public boolean isEmpty(){ return heap.isEmpty(); }

    @Override
    public String toString() { return heap.toString(); }
}
