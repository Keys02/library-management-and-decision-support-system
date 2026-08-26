import algorithms.Sorting;
import algorithms.Searching;
import algorithms.Greedy;
import algorithms.DynamicProgramming;
import datastructures.linear.DynamicArray;
import model.Resource;
import model.ResourceType;

public class TestAlgorithms {
    public static void main(String[] args) {
        testSelectionSort();
        testInsertionSort();
        testMergeSort();
        testQuickSort();
        testLinearSearchFound();
        testLinearSearchNotFound();
        testBinarySearchFound();
        testBinarySearchNotFound();
        testGreedy();
        testKnapsack();
        System.out.println("\n All algorithm tests passed!");
    }

    static DynamicArray<Integer> arr(int... vals) {
        DynamicArray<Integer> a = new DynamicArray<>();
        for (int v : vals) a.add(v);
        return a;
    }

    static void testSelectionSort() {
        System.out.println("Testing Selection Sort...");
        DynamicArray<Integer> a = arr(5, 3, 8, 1, 9, 2);
        Sorting.selectionSort(a);
        assert a.get(0) == 1 : "First should be 1";
        assert a.get(5) == 9 : "Last should be 9";
        assert a.get(2) == 3 : "Third should be 3";
        System.out.println("  Selection Sort");
    }

    static void testInsertionSort() {
        System.out.println("Testing Insertion Sort...");
        DynamicArray<Integer> a = arr(7, 2, 4, 1, 5);
        Sorting.insertionSort(a);
        assert a.get(0) == 1 : "First should be 1";
        assert a.get(4) == 7 : "Last should be 7";
        System.out.println("  Insertion Sort");
    }

    static void testMergeSort() {
        System.out.println("Testing Merge Sort...");
        DynamicArray<Integer> a = arr(38, 27, 43, 3, 9, 82, 10);
        Sorting.mergeSort(a, 0, a.size() - 1);
        assert a.get(0) == 3  : "First should be 3";
        assert a.get(6) == 82 : "Last should be 82";
        assert a.get(3) == 27 : "Fourth should be 27";
        System.out.println("  Merge Sort");
    }

    static void testQuickSort() {
        System.out.println("Testing Quick Sort...");
        DynamicArray<Integer> a = arr(10, 7, 8, 9, 1, 5);
        Sorting.quickSort(a, 0, a.size() - 1);
        assert a.get(0) == 1  : "First should be 1";
        assert a.get(5) == 10 : "Last should be 10";
        System.out.println("  Quick Sort");
    }

    static void testLinearSearchFound() {
        System.out.println("Testing Linear Search (found)...");
        DynamicArray<Integer> a = arr(3, 7, 1, 9, 4);
        int idx = Searching.linearSearch(a, 9);
        assert idx == 3 : "Should find 9 at index 3";
        System.out.println("  Linear Search found");
    }

    static void testLinearSearchNotFound() {
        System.out.println("Testing Linear Search (not found)...");
        DynamicArray<Integer> a = arr(3, 7, 1, 9, 4);
        int idx = Searching.linearSearch(a, 99);
        assert idx == -1 : "Should return -1 for missing value";
        System.out.println("  Linear Search not found");
    }

    static void testBinarySearchFound() {
        System.out.println("Testing Binary Search (found)...");
        DynamicArray<Integer> a = arr(1, 3, 5, 7, 9, 11);
        int idx = Searching.binarySearch(a, 7);
        assert idx == 3 : "Should find 7 at index 3";
        System.out.println("  Binary Search found");
    }

    static void testBinarySearchNotFound() {
        System.out.println("Testing Binary Search (not found)...");
        DynamicArray<Integer> a = arr(1, 3, 5, 7, 9, 11);
        int idx = Searching.binarySearch(a, 6);
        assert idx == -1 : "Should return -1 for missing value";
        System.out.println("  Binary Search not found");
    }

    static void testGreedy() {
        System.out.println("Testing Greedy resource selection...");
        DynamicArray<Resource> resources = new DynamicArray<>();
        resources.add(new Resource(1, "Laptop", ResourceType.EQUIPMENT, 3500.0, 1, 95));
        resources.add(new Resource(2, "Projector", ResourceType.EQUIPMENT, 1800.0, 1, 80));
        resources.add(new Resource(3, "Whiteboard", ResourceType.EQUIPMENT, 400.0, 1, 65));
        DynamicArray<Resource> selected = Greedy.greedyByValue(resources, 4000.0);
        assert selected.size() > 0 : "Should select at least one resource";
        assert selected.get(0).getName().equals("Laptop") : "Highest value should be Laptop";
        System.out.println("  Greedy");
    }

    static void testKnapsack() {
        System.out.println("Testing Knapsack DP...");
        DynamicArray<Resource> resources = new DynamicArray<>();
        resources.add(new Resource(1, "Laptop", ResourceType.EQUIPMENT, 3500.0, 1, 95));
        resources.add(new Resource(2, "Projector", ResourceType.EQUIPMENT, 1800.0, 1, 80));
        resources.add(new Resource(3, "Whiteboard", ResourceType.EQUIPMENT, 400.0, 1, 65));
        resources.add(new Resource(4, "Scanner", ResourceType.EQUIPMENT, 800.0, 1, 70));
        DynamicArray<Resource> chosen = DynamicProgramming.knapsack(resources, 3000);
        assert chosen.size() > 0 : "Should select at least one resource";
        double totalCost = 0;
        for (int i = 0; i < chosen.size(); i++) totalCost += chosen.get(i).getCost();
        assert totalCost <= 3000 : "Total cost should not exceed budget";
        System.out.println("  Knapsack DP");
    }
}