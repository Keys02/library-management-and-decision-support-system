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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Dijkstra Result\n");
        sb.append("Source: ").append(sourceId).append("\n");

        for (int i = 0; i < dist.length; i++) {

            if (dist[i] == Double.MAX_VALUE) {
                sb.append("Library ")
                .append(i)
                .append(": unreachable\n");
                continue;
            }

            sb.append("Library ")
            .append(i)
            .append(": distance = ")
            .append(dist[i])
            .append(", path = ");

            LinkedList<Integer> path = pathTo(i);

            for (int j = 0; j < path.size(); j++) {
                if (j > 0) {
                    sb.append(" -> ");
                }
                sb.append(path.get(j));
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}
