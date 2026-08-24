package algorithms.sorting;

public class SelectionSort {

    public static <T extends Comparable<T>> void sort(T[] array) {

        for (int i = 0; i < array.length - 1; i++) {

            int smallest = i;

            for (int j = i + 1; j < array.length; j++) {

                if (array[j].compareTo(array[smallest]) < 0) {
                    smallest = j;
                }

            }

            T temp = array[i];
            array[i] = array[smallest];
            array[smallest] = temp;
        }
    }
}