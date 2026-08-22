package algorithms.sorting;

public class MergeSort {

    public static <T extends Comparable<T>> void sort(T[] array) {
        mergeSort(array, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void mergeSort(
            T[] array,
            int left,
            int right) {

        if (left >= right)
            return;

        int middle = (left + right) / 2;

        mergeSort(array, left, middle);
        mergeSort(array, middle + 1, right);

        merge(array, left, middle, right);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(
            T[] array,
            int left,
            int middle,
            int right) {

        Object[] temp = new Object[right - left + 1];

        int i = left;
        int j = middle + 1;
        int k = 0;

        while (i <= middle && j <= right) {

            if (array[i].compareTo(array[j]) <= 0) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }

        }

        while (i <= middle)
            temp[k++] = array[i++];

        while (j <= right)
            temp[k++] = array[j++];

        for (int m = 0; m < temp.length; m++) {
            array[left + m] = (T) temp[m];
        }
    }
}