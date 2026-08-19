package test.hash;

import datastructures.hash.HashTable;

public class HashTableTest {

    public static void main(String[] args) {

        HashTable<String, String> table = new HashTable<>();

        table.put("CS101", "Algorithms");
        table.put("CS102", "Data Structures");
        table.put("CS103", "Operating Systems");

        System.out.println("Initial Size: " + table.size());

        System.out.println();

        System.out.println("Removing CS102...");
        System.out.println("Removed: " + table.remove("CS102"));

        System.out.println();

        System.out.println("New Size: " + table.size());

        System.out.println();

        System.out.println("CS101 -> " + table.get("CS101"));
        System.out.println("CS102 -> " + table.get("CS102"));
        System.out.println("CS103 -> " + table.get("CS103"));

        System.out.println();

        System.out.println("Contains CS101: " + table.containsKey("CS101"));
        System.out.println("Contains CS200: " + table.containsKey("CS200"));

        System.out.println();

        System.out.println("Load Factor: " + table.getLoadFactor());

        System.out.println("Collisions: " + table.countCollisions());
    }
}