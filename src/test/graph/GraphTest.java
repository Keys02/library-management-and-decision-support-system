package test.graph;

import datastructures.graph.Graph;

public class GraphTest {

    public static void main(String[] args) {

        Graph<String> graph = new Graph<>();

        graph.addVertex("Balme Library");
        graph.addVertex("Commonwealth Hall");
        graph.addVertex("Legon Hall");

        graph.addEdge(0, 1, 2.5);
        graph.addEdge(0, 2, 4.1);

        System.out.println("Vertices and Connections");

        for (int i = 0; i < graph.size(); i++) {

            System.out.println(graph.getVertex(i));

            System.out.println(
                "Connections: " +
                graph.getVertex(i).getEdges().size()
            );

            System.out.println();
        }
    }
}