package test.sorting;

import algorithms.sorting.QuickSort;

public class QuickSortTest {

    public static void main(String[] args) {

        Integer[] numbers = {
                68,
                25,
                42,
                8,
                12
        };

        QuickSort.sort(numbers);

        System.out.println("Sorted Array");

        for (Integer number : numbers) {
            System.out.print(number + " ");
        }

    }

}