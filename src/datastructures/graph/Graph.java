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

                int neighbourIndex = getVertexIndex(neighbour);

                if (!visited[neighbourIndex]) {

                    visited[neighbourIndex] = true;

                    queue.enqueue(neighbour);
                }
            }
        }
    }

    private int getVertexIndex(GraphNode<T> node) {

        for (int i = 0; i < vertices.size(); i++) {

            if (vertices.get(i) == node) {
                return i;
            }

        }

        return -1;
    }

    public void dfs(int startIndex) {

        boolean[] visited = new boolean[vertices.size()];

        LinkedStack<GraphNode<T>> stack = new LinkedStack<>();

        stack.push(vertices.get(startIndex));

        while (!stack.isEmpty()) {

            GraphNode<T> current = stack.pop();

            int currentIndex = getVertexIndex(current);

            if (visited[currentIndex]) {
                continue;
            }

            visited[currentIndex] = true;

            System.out.println(current);

            for (int i = current.getEdges().size() - 1; i >= 0; i--) {

                GraphEdge<T> edge = current.getEdges().get(i);

                GraphNode<T> neighbour = edge.getDestination();

                int neighbourIndex = getVertexIndex(neighbour);

                if (!visited[neighbourIndex]) {
                    stack.push(neighbour);
                }
            }
        }
    }
}