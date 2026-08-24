package test.searching;

import algorithms.searching.LinearSearch;

public class LinearSearchTest {

    public static void main(String[] args) {

        String[] books = {

                "Algorithms",
                "Database",
                "Java",
                "Networks",
                "Operating Systems"

        };

        int index =
                LinearSearch.search(
                        books,
                        "Java"
                );

        System.out.println(
                "Found at index: "
                        + index
        );

    }

}