package datastructures.graph;

public class GraphEdge<T> {

    private final GraphNode<T> destination;

    private final double weight;

    public GraphEdge(GraphNode<T> destination,
                     double weight) {

        this.destination = destination;
        this.weight = weight;
    }

    public GraphNode<T> getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }
}