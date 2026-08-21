package datastructures.graph;
import datastructures.linear.ArrayQueue;
import datastructures.linear.DynamicArray;
import datastructures.linear.LinkedStack;

public class Graph<T> {

    private final DynamicArray<GraphNode<T>> vertices;

    public Graph() {
        vertices = new DynamicArray<>();
    }

    public void addVertex(T data) {
       GraphNode<T> node =
        new GraphNode<>(vertices.size(), data);

        vertices.add(node);
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

    public void addUndirectedEdge(int sourceIndex,
                              int destinationIndex,
                              double weight) {

        addEdge(sourceIndex, destinationIndex, weight);
        addEdge(destinationIndex, sourceIndex, weight);
    }

    public void bfs(int startIndex) {

        boolean[] visited = new boolean[vertices.size()];

        ArrayQueue<GraphNode<T>> queue = new ArrayQueue<>();

        GraphNode<T> start = vertices.get(startIndex);

        visited[startIndex] = true;

        queue.enqueue(start);

        while (!queue.isEmpty()) {

            GraphNode<T> current = queue.dequeue();

            System.out.println(current);

            for (int i = 0; i < current.getEdges().size(); i++) {

                GraphEdge<T> edge = current.getEdges().get(i);

               GraphNode<T> neighbour = edge.getDestination();

               int neighbourIndex = neighbour.getId();
                if (!visited[neighbourIndex]) {

                    visited[neighbourIndex] = true;

                    queue.enqueue(neighbour);
                }
            }
        }
    }



    public void dfs(int startIndex) {

        boolean[] visited = new boolean[vertices.size()];

        LinkedStack<GraphNode<T>> stack = new LinkedStack<>();

        stack.push(vertices.get(startIndex));

        while (!stack.isEmpty()) {

            GraphNode<T> current = stack.pop();

            int currentIndex = current.getId();

            if (visited[currentIndex]) {
                continue;
            }

            visited[currentIndex] = true;

            System.out.println(current);

            for (int i = current.getEdges().size() - 1; i >= 0; i--) {

                GraphEdge<T> edge = current.getEdges().get(i);

               GraphNode<T> neighbour = edge.getDestination();

               int neighbourIndex = neighbour.getId();

                if (!visited[neighbourIndex]) {
                    stack.push(neighbour);
                }
            }
        }
    }

    public double[] initializeDistances(int startIndex) {

        double[] distances = new double[vertices.size()];

        for (int i = 0; i < distances.length; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
        }

        distances[startIndex] = 0.0;

        return distances;
    }

    private void relaxEdge(double[] distances,
                       int currentIndex,
                       GraphEdge<T> edge) {

        int neighbourIndex = edge.getDestination().getId();
        double newDistance =
                distances[currentIndex] + edge.getWeight();

        if (newDistance < distances[neighbourIndex]) {

            distances[neighbourIndex] = newDistance;

        }
    }

    public double[] relaxNeighbours(int startIndex) {

        double[] distances = initializeDistances(startIndex);

        GraphNode<T> start = vertices.get(startIndex);

        for (int i = 0; i < start.getEdges().size(); i++) {

            relaxEdge(
                    distances,
                    startIndex,
                    start.getEdges().get(i)
            );

        }

        return distances;
    }

    private int getClosestUnvisited(double[] distances, boolean[] visited) {

        double smallest = Double.POSITIVE_INFINITY;
        int index = -1;

        for (int i = 0; i < distances.length; i++) {

            if (!visited[i] && distances[i] < smallest) {
                smallest = distances[i];
                index = i;
            }
        }

        return index;
    }

    public double[] dijkstra(int startIndex) {

        double[] distances = initializeDistances(startIndex);

        boolean[] visited = new boolean[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {

            int current = getClosestUnvisited(distances, visited);

            if (current == -1) {
                break;
            }

            visited[current] = true;

            GraphNode<T> node = vertices.get(current);

            for (int j = 0; j < node.getEdges().size(); j++) {

                GraphEdge<T> edge = node.getEdges().get(j);

                int neighbour = edge.getDestination().getId();

                if (!visited[neighbour]) {

                    double newDistance =
                            distances[current] + edge.getWeight();

                    if (newDistance < distances[neighbour]) {

                        distances[neighbour] = newDistance;

                    }

                }

            }

        }

        return distances;
    }
}