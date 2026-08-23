package datastructures.graph;

import datastructures.linear.LinkedList;

/**
 * Holds the output of Dijkstra's algorithm: distances and predecessor map.
 */
public class DijkstraResult {

    private final int      sourceId;
    private final double[] dist;
    private final int[]    pred;

    public DijkstraResult(int sourceId, double[] dist, int[] pred) {
        this.sourceId = sourceId;
        this.dist     = dist;
        this.pred     = pred;
    }

    public double distanceTo(int targetId) {
        return dist[targetId];
    }

    public boolean isReachable(int targetId) {
        return dist[targetId] != Double.MAX_VALUE;
    }

    /** Reconstructs the path from source → target as a list of library IDs. */
    public LinkedList<Integer> pathTo(int targetId) {
        LinkedList<Integer> path = new LinkedList<>();
        if (!isReachable(targetId)) return path;
        for (int at = targetId; at != -1; at = pred[at])
            path.addFirst(at);
        return path;
    }

    public int getSourceId() { return sourceId; }
}
