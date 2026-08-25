package algorithms;

import datastructures.linear.DynamicArray;

/**
 * Linear and binary search over a DynamicArray<Integer>.
 */
public class Searching {

    // ── Linear Search  O(n) ────────────────────────

    public static int linearSearch(DynamicArray<Integer> arr, int target) {
        for (int i = 0; i < arr.size(); i++)
            if (arr.get(i) == target) return i;
        return -1;
    }

    // ── Binary Search  O(log n) — array must be sorted ───

    public static int binarySearch(DynamicArray<Integer> arr, int target) {
        int lo = 0, hi = arr.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            int val = arr.get(mid);
            if      (val == target) return mid;
            else if (val < target)  lo = mid + 1;
            else                    hi = mid - 1;
        }
        return -1;
    }
}
