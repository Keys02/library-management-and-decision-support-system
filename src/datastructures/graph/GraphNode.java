package datastructures.graph;

import datastructures.linear.LinkedList;

public class GraphNode<T> {

    private final T data;

    private final LinkedList<GraphEdge<T>> edges;

    public GraphNode(T data) {

        this.data = data;

        this.edges = new LinkedList<>();

    }

    public T getData() {
        return data;
    }

    public LinkedList<GraphEdge<T>> getEdges() {
        return edges;
    }

    public void addEdge(GraphEdge<T> edge) {
        edges.addLast(edge);
    }

    @Override
    public String toString() {
        return data.toString();
    }
}