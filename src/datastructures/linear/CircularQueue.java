package datastructures.linear;

import datastructures.interfaces.Queue;

public class CircularQueue<T> implements Queue<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int front;
    private int rear;
    private int size;

    public CircularQueue() {
        elements = new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
        size = 0;
    }

    @Override
    public void enqueue(T item) {

        if (size == elements.length) {
            resize();
        }

        elements[rear] = item;
        rear = (rear + 1) % elements.length;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T dequeue() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        T item = (T) elements[front];
        elements[front] = null;

        front = (front + 1) % elements.length;
        size--;

        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        return (T) elements[front];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {

        elements = new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
        size = 0;
    }

    private void resize() {

        Object[] newArray = new Object[elements.length * 2];

        for (int i = 0; i < size; i++) {
            newArray[i] = elements[(front + i) % elements.length];
        }

        elements = newArray;
        front = 0;
        rear = size;
    }
}