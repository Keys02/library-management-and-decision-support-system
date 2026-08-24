package test.linear;

import datastructures.interfaces.Iterator;
import datastructures.linear.LinkedList;

public class LinkedListTest {

    public static void main(String[] args) {

        LinkedList<String> books = new LinkedList<>();

        books.addLast("Java");
        books.addLast("Algorithms");
        books.addLast("Python");

        System.out.println("Linked List:");
        System.out.println(books);

        System.out.println();

        System.out.println("Iterator Traversal:");

        Iterator<String> iterator = books.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

    }
}