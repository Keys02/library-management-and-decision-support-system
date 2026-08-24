package algorithms;

import datastructures.linear.DynamicArray;

/**
 * All sorting algorithms operate on a DynamicArray<Integer> in-place.
 * No java.util.* used.
 */
public class Sorting {

    // ── Selection Sort  O(n²) ──────────────────────

    public static void selectionSort(DynamicArray<Integer> arr) {
        int n = arr.size();
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++)
                if (arr.get(j) < arr.get(minIdx)) minIdx = j;
            arr.swap(i, minIdx);
        }
    }

    // ── Insertion Sort  O(n²) ──────────────────────

    public static void insertionSort(DynamicArray<Integer> arr) {
        int n = arr.size();
        for (int i = 1; i < n; i++) {
            int key = arr.get(i), j = i - 1;
            while (j >= 0 && arr.get(j) > key) {
                arr.set(j + 1, arr.get(j)); j--;
            }
            arr.set(j + 1, key);
        }
    }

    // ── Merge Sort  O(n log n) ─────────────────────

    public static void mergeSort(DynamicArray<Integer> arr, int left, int right) {
        if (left >= right) return;
        int mid = (left + right) / 2;
        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }

    private static void merge(DynamicArray<Integer> arr, int left, int mid, int right) {
        int n1 = mid - left + 1, n2 = right - mid;
        int[] L = new int[n1], R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = arr.get(left + i);
        for (int j = 0; j < n2; j++) R[j] = arr.get(mid + 1 + j);
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) arr.set(k++, L[i] <= R[j] ? L[i++] : R[j++]);
        while (i < n1) arr.set(k++, L[i++]);
        while (j < n2) arr.set(k++, R[j++]);
    }

    // ── Quicksort  O(n log n) average ─────────────

    public static void quickSort(DynamicArray<Integer> arr, int low, int high) {
        if (low < high) {
            int p = partition(arr, low, high);
            quickSort(arr, low,  p - 1);
            quickSort(arr, p + 1, high);
        }
    }

    private static int partition(DynamicArray<Integer> arr, int low, int high) {
        int pivot = arr.get(high), i = low - 1;
        for (int j = low; j < high; j++)
            if (arr.get(j) <= pivot) arr.swap(++i, j);
        arr.swap(i + 1, high);
        return i + 1;
    }
}
