package test.sorting;

import algorithms.sorting.SelectionSort;

public class SelectionSortTest {

    public static void main(String[] args) {

        Integer[] numbers = {

                68,
                25,
                42,
                8,
                12

        };

        SelectionSort.sort(numbers);

        System.out.println("Sorted Array");

        for (Integer number : numbers) {
            System.out.print(number + " ");
        }

    }

}