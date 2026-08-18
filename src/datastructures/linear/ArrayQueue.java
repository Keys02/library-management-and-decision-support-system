package datastructures.linear;

import datastructures.interfaces.Queue;

public class ArrayQueue<T> implements Queue<T> {

    private final DynamicArray<T> elements;

    public ArrayQueue() {
        elements = new DynamicArray<>();
    }

    @Override
    public void enqueue(T item) {
        elements.add(item);
    }

    @Override
    public T dequeue() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        return elements.remove(0);
    }

    @Override
    public T peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        return elements.get(0);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}