package datastructures.linear;

import datastructures.interfaces.Iterator;

public class LinkedListIterator<T> implements Iterator<T> {

    private Node<T> current;

    public LinkedListIterator(Node<T> head) {
        this.current = head;
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public T next() {

        T data = current.data;

        current = current.next;

        return data;
    }
}