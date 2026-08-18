package test;

import datastructures.linear.LinkedList;

public class LinkedListTest {

    public static void main(String[] args) {

        LinkedList<String> books = new LinkedList<>();

        books.addFirst("Algorithms");
        books.addFirst("Java");
        books.addLast("Python");

        System.out.println("Original:");
        System.out.println(books);

        books.set(1, "Data Structures");

        System.out.println("\nAfter set:");
        System.out.println(books);

        System.out.println("\nContains Java?");
        System.out.println(books.contains("Java"));

        System.out.println("\nIndex of Python:");
        System.out.println(books.indexOf("Python"));

        System.out.println("\nRemoved:");
        System.out.println(books.remove(1));

        System.out.println("\nFinal List:");
        System.out.println(books);

        System.out.println("\nSize:");
        System.out.println(books.size());
    }
}