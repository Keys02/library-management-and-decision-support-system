package datastructures.heap;

import datastructures.interfaces.PriorityItem;

/**
 * Priority queue backed by MaxHeap<T>.
 * ServiceRequest implements PriorityItem so it can be enqueued here.
 */
public class PriorityQueue<T extends PriorityItem> {

    private final MaxHeap<T> heap;

    public PriorityQueue() { heap = new MaxHeap<>(); }

    public void enqueue(T item) { heap.insert(item); }
    public T    dequeue()       { return heap.extractMax(); }
    public T    peek()          { return heap.peek(); }
    public boolean isEmpty()    { return heap.isEmpty(); }
    public int  size()          { return heap.size(); }

    @Override
    public String toString() { return heap.toString(); }
}
