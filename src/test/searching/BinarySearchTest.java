package test.searching;

import algorithms.searching.BinarySearch;

public class BinarySearchTest {

    public static void main(String[] args) {

        Integer[] numbers = {

                10,
                20,
                30,
                40,
                50,
                60,
                70

        };

        int index =
                BinarySearch.search(
                        numbers,
                        60
                );

        System.out.println(
                "Found at index: "
                        + index
        );

    }

}