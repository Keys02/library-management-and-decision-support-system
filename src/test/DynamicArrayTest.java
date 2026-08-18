package test;

import datastructures.linear.DynamicArray;

public class DynamicArrayTest {

    public static void main(String[] args) {

        DynamicArray<String> books = new DynamicArray<>();

        books.add("Java");
        books.add("Python");
        books.add("Algorithms");

        System.out.println("Books:");
        System.out.println(books);

        System.out.println();

        System.out.println("First Book:");
        System.out.println(books.get(0));

        System.out.println();

        books.remove(1);

        System.out.println("After Removing Python:");
        System.out.println(books);

        System.out.println();

        System.out.println("Size:");
        System.out.println(books.size());

    }
}