package datastructures.interfaces;

public interface List<T> {

    void add(T item);

    T get(int index);

    void set(int index, T item);

    T remove(int index);

    boolean contains(T item);

    int indexOf(T item);

    int size();

    boolean isEmpty();

    void clear();
}