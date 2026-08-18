package datastructures.linear;

import datastructures.interfaces.Deque;

public class ArrayDeque<T> implements Deque<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int front;
    private int rear;
    private int size;

    public ArrayDeque() {
        elements = new Object[DEFAULT_CAPACITY];
        front = 0;
        rear = 0;
        size = 0;
    }

    @Override
    public void addFirst(T item) {

        if (size == elements.length) {
            resize();
        }

        front = (front - 1 + elements.length) % elements.length;
        elements[front] = item;
        size++;
    }

    @Override
    public void addLast(T item) {

        if (size == elements.length) {
            resize();
        }

        elements[rear] = item;
        rear = (rear + 1) % elements.length;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T removeFirst() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }

        T item = (T) elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        size--;

        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T removeLast() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }

        rear = (rear - 1 + elements.length) % elements.length;

        T item = (T) elements[rear];
        elements[rear] = null;
        size--;

        return item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peekFirst() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }

        return (T) elements[front];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T peekLast() {

        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }

        int last = (rear - 1 + elements.length) % elements.length;
        return (T) elements[last];
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
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