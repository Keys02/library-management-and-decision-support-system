package datastructures.interfaces;

public interface Deque<T> {

    void addFirst(T item);

    void addLast(T item);

    T removeFirst();

    T removeLast();

    T peekFirst();

    T peekLast();

    boolean isEmpty();

    int size();
}