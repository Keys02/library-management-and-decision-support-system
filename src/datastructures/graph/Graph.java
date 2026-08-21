package datastructures.graph;

import datastructures.linear.DynamicArray;

public class Graph<T> {

    private final DynamicArray<GraphNode<T>> vertices;

    public Graph() {
        vertices = new DynamicArray<>();
    }

    public void addVertex(T data) {
        vertices.add(new GraphNode<>(data));
    }

    public void addEdge(int sourceIndex,
                        int destinationIndex,
                        double weight) {

        GraphNode<T> source = vertices.get(sourceIndex);
        GraphNode<T> destination = vertices.get(destinationIndex);

        source.addEdge(new GraphEdge<>(destination, weight));
    }

    public int size() {
        return vertices.size();
    }

    public GraphNode<T> getVertex(int index) {
        return vertices.get(index);
    }
}