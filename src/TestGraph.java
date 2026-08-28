import datastructures.graph.Graph;
import datastructures.graph.DijkstraResult;
import datastructures.linear.LinkedList;
import datastructures.linear.DynamicArray;
import model.Library;
import model.Road;

public class TestGraph {
    public static void main(String[] args) {
        testAddLibraryAndRoad();
        testBFS();
        testDFS();
        testDijkstraDistance();
        testDijkstraPath();
        testDijkstraUnreachable();
        testKruskal();
        testPrim();
        testVertexCount();
        testEdgeCount();
        System.out.println("\n✅ All graph tests passed!");
    }

    static Graph buildGraph() {
        Graph g = new Graph();
        g.addLibrary(new Library(1, "Accra", "Greater Accra", "8am-6pm"));
        g.addLibrary(new Library(2, "Kumasi", "Ashanti", "8am-6pm"));
        g.addLibrary(new Library(3, "Tamale", "Northern", "8am-6pm"));
        g.addLibrary(new Library(4, "Cape Coast", "Central", "8am-6pm"));
        g.addLibrary(new Library(5, "Sunyani", "Bono", "8am-6pm"));
        g.addRoad(new Road(1, 1, 2, 250.0, 3.5));
        g.addRoad(new Road(2, 1, 4, 165.0, 2.5));
        g.addRoad(new Road(3, 2, 3, 390.0, 5.5));
        g.addRoad(new Road(4, 2, 5, 190.0, 3.0));
        g.addRoad(new Road(5, 5, 3, 200.0, 3.0));
        return g;
    }

    static void testAddLibraryAndRoad() {
        System.out.println("Testing addLibrary and addRoad...");
        Graph g = buildGraph();
        assert g.getLibrary(1) != null : "Library 1 should exist";
        assert g.getLibrary(3) != null : "Library 3 should exist";
        assert g.getNeighbours(1).size() == 2 : "Accra should have 2 neighbours";
        assert g.getNeighbours(2).size() == 3 : "Kumasi should have 3 neighbours";
        System.out.println("  addLibrary/addRoad ✅");
    }

    static void testBFS() {
        System.out.println("Testing BFS...");
        Graph g = buildGraph();
        DynamicArray<Integer> bfs = g.bfs(1);
        assert bfs.size() == 5 : "BFS should visit all 5 nodes";
        assert bfs.get(0) == 1 : "BFS should start at node 1";
        System.out.println("  BFS ✅");
    }

    static void testDFS() {
        System.out.println("Testing DFS...");
        Graph g = buildGraph();
        DynamicArray<Integer> dfs = g.dfs(1);
        assert dfs.size() == 5 : "DFS should visit all 5 nodes";
        assert dfs.get(0) == 1 : "DFS should start at node 1";
        System.out.println("  DFS ✅");
    }

    static void testDijkstraDistance() {
        System.out.println("Testing Dijkstra distance...");
        Graph g = buildGraph();
        DijkstraResult result = g.dijkstra(1);
        assert result.distanceTo(2) == 3.5 : "Accra to Kumasi should be 3.5hrs";
        assert result.distanceTo(4) == 2.5 : "Accra to Cape Coast should be 2.5hrs";
        assert result.distanceTo(3) == 9.0 : "Accra to Tamale should be 9.0hrs";
        System.out.println("  Dijkstra distance ✅");
    }

    static void testDijkstraPath() {
        System.out.println("Testing Dijkstra path reconstruction...");
        Graph g = buildGraph();
        DijkstraResult result = g.dijkstra(1);
        LinkedList<Integer> path = result.pathTo(3);
        assert path.size() == 3 : "Path from Accra to Tamale should have 3 nodes";
        assert path.get(0) == 1 : "Path should start at Accra";
        assert path.get(2) == 3 : "Path should end at Tamale";
        System.out.println("  Dijkstra path ✅");
    }

    static void testDijkstraUnreachable() {
        System.out.println("Testing Dijkstra unreachable node...");
        Graph g = buildGraph();
        g.addLibrary(new Library(99, "Isolated", "Unknown", "9am-5pm"));
        DijkstraResult result = g.dijkstra(1);
        assert !result.isReachable(99) : "Isolated node should be unreachable";
        System.out.println("  Dijkstra unreachable ✅");
    }

    static void testKruskal() {
        System.out.println("Testing Kruskal MST...");
        Graph g = buildGraph();
        LinkedList<Road> mst = g.kruskal();
        assert mst.size() == 4 : "MST of 5 nodes should have 4 edges";
        System.out.println("  Kruskal ✅");
    }

    static void testPrim() {
        System.out.println("Testing Prim MST...");
        Graph g = buildGraph();
        LinkedList<Road> mst = g.prim(1);
        assert mst.size() == 4 : "Prim MST of 5 nodes should have 4 edges";
        System.out.println("  Prim ✅");
    }

    static void testVertexCount() {
        System.out.println("Testing vertex count...");
        Graph g = buildGraph();
        assert g.vertexCount() == 5 : "Should have 5 vertices";
        System.out.println("  Vertex count ✅");
    }

    static void testEdgeCount() {
        System.out.println("Testing edge count...");
        Graph g = buildGraph();
        assert g.edgeCount() == 5 : "Should have 5 edges";
        System.out.println("  Edge count ✅");
    }
}