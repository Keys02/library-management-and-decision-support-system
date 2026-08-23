package datastructures.graph;
import datastructures.linear.ArrayQueue;
import datastructures.linear.DynamicArray;
import datastructures.linear.LinkedStack;

<<<<<<< HEAD
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
=======
import datastructures.hash.HashTable;
import datastructures.linear.LinkedList;
import datastructures.linear.DynamicArray;
import datastructures.linear.ArrayQueue;
import model.Library;
import model.Road;

/**
 * Weighted undirected graph of library branches connected by roads.
 * Uses our custom HashTable + LinkedList — no java.util.* collections.
 */
public class Graph {

    private final HashTable<Integer, Library>          libraries;
    private final HashTable<Integer, LinkedList<Road>> adjacencyList;
    private int edgeCount;

    public Graph() {
        libraries     = new HashTable<>();
        adjacencyList = new HashTable<>();
        edgeCount     = 0;
    }

    // ── Mutation ───────────────────────────────────

    public void addLibrary(Library library) {
        libraries.put(library.getId(), library);
        if (!adjacencyList.containsKey(library.getId()))
            adjacencyList.put(library.getId(), new LinkedList<>());
    }

    public void addRoad(Road road) {
        ensureVertex(road.getSourceLibraryId());
        ensureVertex(road.getDestinationLibraryId());
        adjacencyList.get(road.getSourceLibraryId()).addLast(road);
        adjacencyList.get(road.getDestinationLibraryId()).addLast(road);
        edgeCount++;
    }

    private void ensureVertex(int id) {
        if (!adjacencyList.containsKey(id))
            adjacencyList.put(id, new LinkedList<>());
    }

    // ── Accessors ──────────────────────────────────

    public Library getLibrary(int id)             { return libraries.get(id); }
    public LinkedList<Road> getNeighbours(int id) { return adjacencyList.get(id); }
    public int vertexCount()                      { return libraries.size(); }
    public int edgeCount()                        { return edgeCount; }

    public DynamicArray<Integer> getAllLibraryIds() {
        DynamicArray<Integer> ids = new DynamicArray<>();
        for (int i = 0; i < 10001; i++)
            if (libraries.containsKey(i)) ids.add(i);
        return ids;
    }

    // ── BFS ────────────────────────────────────────

    public DynamicArray<Integer> bfs(int startId) {
        DynamicArray<Integer>   visited = new DynamicArray<>();
        HashTable<Integer,Boolean> seen = new HashTable<>();
        ArrayQueue<Integer>       queue = new ArrayQueue<>();

        seen.put(startId, true);
        queue.enqueue(startId);

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            visited.add(current);
            LinkedList<Road> nbrs = adjacencyList.get(current);
            if (nbrs == null) continue;
            for (int i = 0; i < nbrs.size(); i++) {
                int next = nbrs.get(i).getOtherLibrary(current);
                if (!seen.containsKey(next)) { seen.put(next, true); queue.enqueue(next); }
            }
        }
        return visited;
    }

    // ── DFS ────────────────────────────────────────

    public DynamicArray<Integer> dfs(int startId) {
        DynamicArray<Integer>   visited = new DynamicArray<>();
        HashTable<Integer,Boolean> seen = new HashTable<>();
        dfsHelper(startId, seen, visited);
        return visited;
    }

    private void dfsHelper(int id, HashTable<Integer,Boolean> seen, DynamicArray<Integer> visited) {
        seen.put(id, true);
        visited.add(id);
        LinkedList<Road> nbrs = adjacencyList.get(id);
        if (nbrs == null) return;
        for (int i = 0; i < nbrs.size(); i++) {
            int next = nbrs.get(i).getOtherLibrary(id);
            if (!seen.containsKey(next)) dfsHelper(next, seen, visited);
        }
    }

    // ── Dijkstra ───────────────────────────────────

    public DijkstraResult dijkstra(int sourceId) {
        DynamicArray<Integer> ids = getAllLibraryIds();
        int n = 10001;
        double[]  dist    = new double[n];
        int[]     pred    = new int[n];
        boolean[] settled = new boolean[n];

        for (int i = 0; i < n; i++) { dist[i] = Double.MAX_VALUE; pred[i] = -1; }
        dist[sourceId] = 0.0;

        int remaining = ids.size();
        while (remaining > 0) {
            int u = -1; double minD = Double.MAX_VALUE;
            for (int i = 0; i < ids.size(); i++) {
                int id = ids.get(i);
                if (!settled[id] && dist[id] < minD) { minD = dist[id]; u = id; }
            }
            if (u == -1) break;
            settled[u] = true; remaining--;

            LinkedList<Road> nbrs = adjacencyList.get(u);
            if (nbrs == null) continue;
            for (int i = 0; i < nbrs.size(); i++) {
                Road r = nbrs.get(i);
                int v = r.getOtherLibrary(u);
                if (settled[v]) continue;
                double nd = dist[u] + r.getTravelTime();
                if (nd < dist[v]) { dist[v] = nd; pred[v] = u; }
            }
        }
        return new DijkstraResult(sourceId, dist, pred);
    }

    // ── Kruskal ────────────────────────────────────

    public LinkedList<Road> kruskal() {
        DynamicArray<Road> allRoads   = new DynamicArray<>();
        DynamicArray<Integer> ids     = getAllLibraryIds();
        HashTable<Integer,Boolean> seen = new HashTable<>();

        for (int i = 0; i < ids.size(); i++) {
            LinkedList<Road> nbrs = adjacencyList.get(ids.get(i));
            if (nbrs == null) continue;
            for (int j = 0; j < nbrs.size(); j++) {
                Road r = nbrs.get(j);
                if (!seen.containsKey(r.getId())) { allRoads.add(r); seen.put(r.getId(), true); }
            }
        }

        // Insertion sort by distance
        for (int i = 1; i < allRoads.size(); i++) {
            Road key = allRoads.get(i); int j = i - 1;
            while (j >= 0 && allRoads.get(j).getDistance() > key.getDistance()) {
                allRoads.set(j + 1, allRoads.get(j)); j--;
            }
            allRoads.set(j + 1, key);
        }

        DisjointSet ds = new DisjointSet();
        for (int i = 0; i < ids.size(); i++) ds.makeSet(ids.get(i));

        LinkedList<Road> mst = new LinkedList<>();
        for (int i = 0; i < allRoads.size(); i++) {
            Road r = allRoads.get(i);
            int u = r.getSourceLibraryId(), v = r.getDestinationLibraryId();
            if (ds.find(u) != ds.find(v)) { ds.union(u, v); mst.addLast(r); }
        }
        return mst;
    }

    // ── Prim ───────────────────────────────────────

    public LinkedList<Road> prim(int startId) {
        DynamicArray<Integer> ids = getAllLibraryIds();
        int n = 10001;
        double[]  key    = new double[n];
        int[]     parent = new int[n];
        boolean[] inMST  = new boolean[n];

        for (int i = 0; i < n; i++) { key[i] = Double.MAX_VALUE; parent[i] = -1; }
        key[startId] = 0.0;

        LinkedList<Road> mst = new LinkedList<>();
        int remaining = ids.size();

        while (remaining > 0) {
            int u = -1; double minKey = Double.MAX_VALUE;
            for (int i = 0; i < ids.size(); i++) {
                int id = ids.get(i);
                if (!inMST[id] && key[id] < minKey) { minKey = key[id]; u = id; }
            }
            if (u == -1) break;
            inMST[u] = true; remaining--;

            if (parent[u] != -1) {
                LinkedList<Road> nbrs = adjacencyList.get(parent[u]);
                Road chosen = null;
                for (int i = 0; i < nbrs.size(); i++) {
                    Road r = nbrs.get(i);
                    if (r.getOtherLibrary(parent[u]) == u)
                        if (chosen == null || r.getDistance() < chosen.getDistance()) chosen = r;
                }
                if (chosen != null) mst.addLast(chosen);
            }

            LinkedList<Road> nbrs = adjacencyList.get(u);
            if (nbrs == null) continue;
            for (int i = 0; i < nbrs.size(); i++) {
                Road r = nbrs.get(i);
                int v = r.getOtherLibrary(u);
                if (!inMST[v] && r.getDistance() < key[v]) { key[v] = r.getDistance(); parent[v] = u; }
            }
        }
        return mst;
    }

    // ── Adjacency matrix ───────────────────────────

    public AdjacencyMatrixResult toAdjacencyMatrix() {
        DynamicArray<Integer> ids = getAllLibraryIds();
        int n = ids.size();
        double[][] matrix = new double[n][n];
        HashTable<Integer,Integer> pos = new HashTable<>();
        for (int i = 0; i < n; i++) pos.put(ids.get(i), i);

        for (int i = 0; i < n; i++) {
            int libId = ids.get(i);
            LinkedList<Road> nbrs = adjacencyList.get(libId);
            if (nbrs == null) continue;
            for (int j = 0; j < nbrs.size(); j++) {
                Road r = nbrs.get(j);
                int other = r.getOtherLibrary(libId);
                if (pos.containsKey(other)) matrix[i][pos.get(other)] = r.getTravelTime();
            }
        }
        return new AdjacencyMatrixResult(ids, matrix);
    }
}
>>>>>>> 7dbbaabf8e7a5dd6feb25d6373427e87fb639351
