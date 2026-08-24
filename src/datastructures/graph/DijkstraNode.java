package datastructures.graph;

public class DijkstraNode {

    private final int vertexIndex;

    private final double distance;

    public DijkstraNode(int vertexIndex,
                        double distance) {

        this.vertexIndex = vertexIndex;
        this.distance = distance;
    }

    public int getVertexIndex() {
        return vertexIndex;
    }

    public double getDistance() {
        return distance;
    }
}