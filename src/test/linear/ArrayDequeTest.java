package test.linear;

import datastructures.linear.ArrayDeque;

public class ArrayDequeTest {

    public static void main(String[] args) {

        ArrayDeque<String> deque = new ArrayDeque<>();

        deque.addLast("B");
        deque.addLast("C");

        deque.addFirst("A");

        deque.addLast("D");

        System.out.println(deque.removeFirst()); // A
        System.out.println(deque.removeLast());  // D
        System.out.println(deque.peekFirst());   // B
        System.out.println(deque.peekLast());    // C
        System.out.println(deque.size());        // 2
    }
}