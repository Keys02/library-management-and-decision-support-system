package datastructures.graph;

import datastructures.linear.DynamicArray;

/** Wraps an adjacency matrix together with the ordered library-ID list. */
public class AdjacencyMatrixResult {
    public final DynamicArray<Integer> ids;
    public final double[][] matrix;

    public AdjacencyMatrixResult(DynamicArray<Integer> ids, double[][] matrix) {
        this.ids    = ids;
        this.matrix = matrix;
    }
}
