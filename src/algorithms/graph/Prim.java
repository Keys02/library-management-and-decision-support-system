package algorithms.graph;

import datastructures.graph.Graph;
import datastructures.graph.GraphEdge;
import datastructures.graph.GraphNode;
import datastructures.linear.DynamicArray;

public class Prim<T> {

   public DynamicArray<WeightedEdge> minimumSpanningTree(
            Graph<T> graph,
            int startIndex) {

        DynamicArray<WeightedEdge> mst =
                new DynamicArray<>();

        boolean[] visited =
                new boolean[graph.size()];

        visited[startIndex] = true;

        while (mst.size() < graph.size() - 1) {

            double smallest =
                    Double.POSITIVE_INFINITY;

            WeightedEdge best = null;

            for (int i = 0; i < graph.size(); i++) {

                if (!visited[i])
                    continue;

                GraphNode<T> node =
                        graph.getVertex(i);

                for (int j = 0;
                    j < node.getEdges().size();
                    j++) {

                    GraphEdge<T> edge =
                            node.getEdges().get(j);

                    int neighbour =
                            edge.getDestination().getId();

                    if (!visited[neighbour]
                            &&
                            edge.getWeight() < smallest) {

                        smallest = edge.getWeight();

                        best = new WeightedEdge(
                                i,
                                neighbour,
                                edge.getWeight());

                    }

                }

            }

            if (best == null)
                break;

            mst.add(best);

            visited[best.getDestination()] = true;

        }

        return mst;
    }

}