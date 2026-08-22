package test.graph;

import algorithms.graph.Kruskal;
import algorithms.graph.WeightedEdge;
import datastructures.linear.DynamicArray;

public class KruskalTest {

    public static void main(String[] args) {

        Kruskal kruskal = new Kruskal();

        DynamicArray<WeightedEdge> edges =
                kruskal.createSampleEdges();

        DynamicArray<WeightedEdge> mst =
                kruskal.minimumSpanningTree(edges, 4);

        System.out.println("Minimum Spanning Tree");

        for (int i = 0; i < mst.size(); i++) {
            System.out.println(mst.get(i));
        }
    }
}