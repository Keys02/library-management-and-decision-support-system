package datastructures.linear;

import datastructures.interfaces.Stack;

public class LinkedStack<T> implements Stack<T> {

    private final LinkedList<T> list;

    public Stack() {
        list = new LinkedList<>();
    }

    @Override
    public void push(T item) {
        list.addFirst(item);
    }

    @Override
    public T pop() {

        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }

        return list.remove(0);
    }

    @Override
    public T peek() {

        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty.");
        }

        return list.get(0);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }
}