package datastructures.heap;

public class PriorityQueue {

    private final MaxHeap heap;

    public PriorityQueue() {
        heap = new MaxHeap();
    }

    public void enqueue(int priority) {
        heap.insert(priority);
    }

    public int dequeue() {
        return heap.extractMax();
    }

    public int peek() {
        return heap.peek();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}