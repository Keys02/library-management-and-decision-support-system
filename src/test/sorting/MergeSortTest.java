package test.sorting;

import algorithms.sorting.MergeSort;

public class MergeSortTest {

    public static void main(String[] args) {

        Integer[] numbers = {
                68,
                25,
                42,
                8,
                12
        };

        MergeSort.sort(numbers);

        System.out.println("Sorted Array");

        for (Integer number : numbers) {
            System.out.print(number + " ");
        }

    }

}