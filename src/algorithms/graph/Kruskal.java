package algorithms.graph;

import datastructures.disjointset.DisjointSet;
import datastructures.linear.DynamicArray;

public class Kruskal {

    public DynamicArray<WeightedEdge> minimumSpanningTree(
            DynamicArray<WeightedEdge> edges,
            int vertices) {

        sortEdges(edges);

        DynamicArray<WeightedEdge> mst =
                new DynamicArray<>();

        DisjointSet ds =
                new DisjointSet(vertices);

        for (int i = 0; i < edges.size(); i++) {

            WeightedEdge edge = edges.get(i);

            if (!ds.connected(edge.getSource(),
                              edge.getDestination())) {

                mst.add(edge);

                ds.union(edge.getSource(),
                        edge.getDestination());

            }

        }

        return mst;
    }

    public DynamicArray<WeightedEdge> createSampleEdges() {

        DynamicArray<WeightedEdge> edges = new DynamicArray<>();

        edges.add(new WeightedEdge(1, 3, 1.2));
        edges.add(new WeightedEdge(0, 1, 2.5));
        edges.add(new WeightedEdge(0, 2, 4.1));
        edges.add(new WeightedEdge(2, 3, 5.0));

        return edges;
    }

    private void sortEdges(DynamicArray<WeightedEdge> edges) {

        for (int i = 1; i < edges.size(); i++) {

            WeightedEdge key = edges.get(i);

            int j = i - 1;

            while (j >= 0 &&
                  edges.get(j).compareTo(key) > 0) {

                edges.set(j + 1, edges.get(j));

                j--;
            }

            edges.set(j + 1, key);
        }
    }
}