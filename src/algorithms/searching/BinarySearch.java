package algorithms.searching;

public class BinarySearch {

    public static <T extends Comparable<T>> int search(
            T[] array,
            T target) {

        int left = 0;
        int right = array.length - 1;

        while (left <= right) {

            int middle = (left + right) / 2;

            int comparison =
                    target.compareTo(array[middle]);

            if (comparison == 0) {
                return middle;
            }

            if (comparison < 0) {

                right = middle - 1;

            } else {

                left = middle + 1;

            }

        }

        return -1;
    }
}