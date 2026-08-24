package datastructures.linear;

import datastructures.interfaces.List;
import datastructures.interfaces.Iterator;

public class LinkedList<T> implements List<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void add(T item) {
        addLast(item);
    }

    public void addFirst(T item) {

        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    public void addLast(T item) {

        Node<T> newNode = new Node<>(item);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // We'll implement these next
    @Override
    public T get(int index) {

        checkIndex(index);

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    @Override
    public void set(int index, T item) {

        checkIndex(index);

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        current.data = item;
    }

    @Override
    public T remove(int index) {

        checkIndex(index);

        Node<T> removed;

        if (index == 0) {

            removed = head;
            head = head.next;

            if (head == null) {
                tail = null;
            }

        } else {

            Node<T> previous = head;

            for (int i = 0; i < index - 1; i++) {
                previous = previous.next;
            }

            removed = previous.next;
            previous.next = removed.next;

            if (removed == tail) {
                tail = previous;
            }
        }

        size--;

        return removed.data;
    }

    @Override
    public boolean contains(T item) {
        return indexOf(item) != -1;
    }

    @Override
    public int indexOf(T item) {

        Node<T> current = head;

        int index = 0;

        while (current != null) {

            if (current.data.equals(item)) {
                return index;
            }

            current = current.next;
            index++;
        }

        return -1;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private void checkIndex(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder("[");

        Node<T> current = head;

        while (current != null) {

            builder.append(current.data);

            if (current.next != null) {
                builder.append(", ");
            }

            current = current.next;
        }

        builder.append("]");

        return builder.toString();
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator<>(head);
    }
}