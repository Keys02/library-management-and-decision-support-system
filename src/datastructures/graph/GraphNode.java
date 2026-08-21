package datastructures.graph;

import datastructures.linear.LinkedList;

public class GraphNode<T> {

    private final int id;
    private final T data;
    private final LinkedList<GraphEdge<T>> edges;

    public GraphNode(int id, T data) {
        this.id = id;
        this.data = data;
        this.edges = new LinkedList<>();
    }

    public int getId() {
        return id;
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