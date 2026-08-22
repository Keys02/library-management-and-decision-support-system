package algorithms.graph;

public class WeightedEdge implements Comparable<WeightedEdge> {

    private final int source;
    private final int destination;
    private final double weight;

    public WeightedEdge(int source,
                        int destination,
                        double weight) {

        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source +
                " -> " +
                destination +
                " (" +
                weight +
                ")";
    }

    @Override
    public int compareTo(WeightedEdge other) {
        return Double.compare(this.weight, other.weight);
    }
}