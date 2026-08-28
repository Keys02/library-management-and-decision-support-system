import algorithms.Sorting;
import algorithms.Searching;
import datastructures.linear.DynamicArray;
import datastructures.hash.HashTable;
import datastructures.heap.IntMaxHeap;
import datastructures.tree.BinarySearchTree;
import datastructures.graph.Graph;
import datastructures.graph.DijkstraResult;
import model.AlgorithmRun;
import model.Library;
import model.Road;
import repository.AlgorithmRunRepository;

import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Runs all 6 performance experiments required by the brief.
 * Results are saved to the algorithm_runs table and exported to data/performance_results.csv.
 *
 * Run this ONCE after DataLoader:
 *   java -cp "out;lib\sqlite-jdbc-3.53.2.1.jar" PerformanceExperiment
 */
public class PerformanceExperiment {

    private static final AlgorithmRunRepository repo = new AlgorithmRunRepository();
    private static final int[] SIZES = {100, 500, 1000, 5000, 10000};
    private static final int RUNS = 3; // run each experiment 3 times, take average

    public static void main(String[] args) {
        System.out.println("=== Performance Experiments Starting ===\n");

        experiment1_SearchComparison();
        experiment2_SortingComparison();
        experiment3_HashTableLoadFactor();
        experiment4_BSTvsSize();
        experiment5_HeapDispatch();
        experiment6_GraphAlgorithms();

        exportToCSV();

        System.out.println("\n=== All experiments complete. Results saved. ===");
    }

    // ── Experiment 1: Linear vs Binary Search ──────────

    static void experiment1_SearchComparison() {
        System.out.println("Experiment 1: Search Comparison");
        for (int size : SIZES) {
            DynamicArray<Integer> arr = generateSorted(size);
            int target = arr.get(size / 2); // always findable

            long linearTotal = 0, binaryTotal = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                Searching.linearSearch(arr, target);
                linearTotal += System.nanoTime() - start;

                start = System.nanoTime();
                Searching.binarySearch(arr, target);
                binaryTotal += System.nanoTime() - start;
            }

            saveRun("LinearSearch",  size, linearTotal / RUNS);
            saveRun("BinarySearch",  size, binaryTotal / RUNS);
            System.out.printf("  n=%5d  Linear=%8dns  Binary=%8dns%n",
                size, linearTotal / RUNS, binaryTotal / RUNS);
        }
        System.out.println();
    }

    // ── Experiment 2: Sorting Comparison ───────────────

    static void experiment2_SortingComparison() {
        System.out.println("Experiment 2: Sorting Comparison");
        for (int size : SIZES) {
            long selTotal = 0, insTotal = 0, merTotal = 0, quiTotal = 0;
            for (int r = 0; r < RUNS; r++) {
                DynamicArray<Integer> a1 = generateRandom(size);
                DynamicArray<Integer> a2 = generateRandom(size);
                DynamicArray<Integer> a3 = generateRandom(size);
                DynamicArray<Integer> a4 = generateRandom(size);

                long start = System.nanoTime();
                Sorting.selectionSort(a1);
                selTotal += System.nanoTime() - start;

                start = System.nanoTime();
                Sorting.insertionSort(a2);
                insTotal += System.nanoTime() - start;

                start = System.nanoTime();
                Sorting.mergeSort(a3, 0, a3.size() - 1);
                merTotal += System.nanoTime() - start;

                start = System.nanoTime();
                Sorting.quickSort(a4, 0, a4.size() - 1);
                quiTotal += System.nanoTime() - start;
            }

            saveRun("SelectionSort", size, selTotal / RUNS);
            saveRun("InsertionSort", size, insTotal / RUNS);
            saveRun("MergeSort",     size, merTotal / RUNS);
            saveRun("QuickSort",     size, quiTotal / RUNS);
            System.out.printf("  n=%5d  Sel=%8dns  Ins=%8dns  Mer=%8dns  Qui=%8dns%n",
                size, selTotal/RUNS, insTotal/RUNS, merTotal/RUNS, quiTotal/RUNS);
        }
        System.out.println();
    }

    // ── Experiment 3: HashTable Load Factor ────────────

    static void experiment3_HashTableLoadFactor() {
        System.out.println("Experiment 3: HashTable Load Factor");
        for (int size : SIZES) {
            long total = 0;
            for (int r = 0; r < RUNS; r++) {
                HashTable<Integer, Integer> table = new HashTable<>();
                long start = System.nanoTime();
                for (int i = 0; i < size; i++) table.put(i, i * 2);
                total += System.nanoTime() - start;
            }
            saveRun("HashTableInsert", size, total / RUNS);
            System.out.printf("  n=%5d  time=%8dns%n", size, total / RUNS);
        }
        System.out.println();
    }

    // ── Experiment 4: BST Insert and Search ────────────

    static void experiment4_BSTvsSize() {
        System.out.println("Experiment 4: BST Insert and Search");
        for (int size : SIZES) {
            long insTotal = 0, srchTotal = 0;
            for (int r = 0; r < RUNS; r++) {
                BinarySearchTree<Integer> bst = new BinarySearchTree<>();
                DynamicArray<Integer> arr = generateRandom(size);

                long start = System.nanoTime();
                for (int i = 0; i < size; i++) bst.insert(arr.get(i));
                insTotal += System.nanoTime() - start;

                start = System.nanoTime();
                bst.contains(arr.get(size / 2));
                srchTotal += System.nanoTime() - start;
            }
            saveRun("BSTInsert", size, insTotal  / RUNS);
            saveRun("BSTSearch", size, srchTotal / RUNS);
            System.out.printf("  n=%5d  Insert=%8dns  Search=%8dns%n",
                size, insTotal/RUNS, srchTotal/RUNS);
        }
        System.out.println();
    }

    // ── Experiment 5: Heap Priority Dispatch ───────────

    static void experiment5_HeapDispatch() {
        System.out.println("Experiment 5: Heap Priority Dispatch");
        for (int size : SIZES) {
            long insTotal = 0, extTotal = 0;
            for (int r = 0; r < RUNS; r++) {
                IntMaxHeap heap = new IntMaxHeap();
                DynamicArray<Integer> arr = generateRandom(size);

                long start = System.nanoTime();
                for (int i = 0; i < size; i++) heap.insert(arr.get(i));
                insTotal += System.nanoTime() - start;

                start = System.nanoTime();
                while (!heap.isEmpty()) heap.extractMax();
                extTotal += System.nanoTime() - start;
            }
            saveRun("HeapInsert",  size, insTotal / RUNS);
            saveRun("HeapExtract", size, extTotal / RUNS);
            System.out.printf("  n=%5d  Insert=%8dns  Extract=%8dns%n",
                size, insTotal/RUNS, extTotal/RUNS);
        }
        System.out.println();
    }

    // ── Experiment 6: Graph Algorithms ─────────────────

    static void experiment6_GraphAlgorithms() {
        System.out.println("Experiment 6: Graph Algorithms");
        int[] graphSizes = {10, 20, 30, 50}; // library counts
        for (int n : graphSizes) {
            Graph g = buildGraph(n);

            long bfsTotal = 0, dfsTotal = 0, dijTotal = 0, mstTotal = 0;
            for (int r = 0; r < RUNS; r++) {
                long start = System.nanoTime();
                g.bfs(1);
                bfsTotal += System.nanoTime() - start;

                start = System.nanoTime();
                g.dfs(1);
                dfsTotal += System.nanoTime() - start;

                start = System.nanoTime();
                g.dijkstra(1);
                dijTotal += System.nanoTime() - start;

                start = System.nanoTime();
                g.kruskal();
                mstTotal += System.nanoTime() - start;
            }
            saveRun("BFS",      n, bfsTotal / RUNS);
            saveRun("DFS",      n, dfsTotal / RUNS);
            saveRun("Dijkstra", n, dijTotal / RUNS);
            saveRun("Kruskal",  n, mstTotal / RUNS);
            System.out.printf("  n=%3d  BFS=%8dns  DFS=%8dns  Dij=%8dns  MST=%8dns%n",
                n, bfsTotal/RUNS, dfsTotal/RUNS, dijTotal/RUNS, mstTotal/RUNS);
        }
        System.out.println();
    }

    // ── Helpers ────────────────────────────────────────

    static void saveRun(String name, int size, long timeNs) {
        long memKb = Runtime.getRuntime().totalMemory()
                   - Runtime.getRuntime().freeMemory();
        memKb = memKb / 1024;
        repo.save(new AlgorithmRun(0, name, size, timeNs, memKb, LocalDateTime.now()));
    }

    static DynamicArray<Integer> generateRandom(int size) {
        DynamicArray<Integer> arr = new DynamicArray<>();
        // Deterministic pseudo-random using linear congruential generator
        long seed = 12345L;
        for (int i = 0; i < size; i++) {
            seed = (seed * 1103515245L + 12345L) & 0x7fffffffL;
            arr.add((int)(seed % 100000));
        }
        return arr;
    }

    static DynamicArray<Integer> generateSorted(int size) {
        DynamicArray<Integer> arr = new DynamicArray<>();
        for (int i = 0; i < size; i++) arr.add(i);
        return arr;
    }

    static Graph buildGraph(int n) {
        Graph g = new Graph();
        for (int i = 1; i <= n; i++)
            g.addLibrary(new Library(i, "Library " + i, "Location " + i, "8am-6pm"));
        int roadId = 1;
        for (int i = 1; i < n; i++) {
            g.addRoad(new Road(roadId++, i, i + 1, 10.0 * i, 0.5 * i));
            if (i + 2 <= n)
                g.addRoad(new Road(roadId++, i, i + 2, 15.0 * i, 0.8 * i));
        }
        return g;
    }

    // ── Export to CSV ──────────────────────────────────

    static void exportToCSV() {
        System.out.println("Exporting results to data/performance_results.csv...");
        try (FileWriter fw = new FileWriter("data/performance_results.csv")) {
            fw.write("algorithm_name,input_size,time_ns,memory_kb,date_run\n");
            var runs = repo.findAll();
            for (int i = 0; i < runs.size(); i++) {
                AlgorithmRun r = runs.get(i);
                fw.write(r.getAlgorithmName() + ","
                    + r.getInputSize() + ","
                    + r.getTimeNs() + ","
                    + r.getMemoryKb() + ","
                    + r.getDateRun() + "\n");
            }
            System.out.println("  Exported " + runs.size() + " records.");
        } catch (IOException e) {
            System.out.println("  Export failed: " + e.getMessage());
        }
    }
}