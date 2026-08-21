package test.graph;

import datastructures.graph.Graph;

public class GraphTest {

    public static void main(String[] args) {

        Graph<String> graph = new Graph<>();

        graph.addVertex("Balme Library");
        graph.addVertex("Commonwealth Hall");
        graph.addVertex("Legon Hall");
        graph.addVertex("Business School");

        graph.addUndirectedEdge(0, 1, 2.5);
        graph.addUndirectedEdge(0, 2, 4.1);
        graph.addUndirectedEdge(1, 3, 1.2);

        System.out.println("BFS Traversal:");

        graph.bfs(0);

    }
}