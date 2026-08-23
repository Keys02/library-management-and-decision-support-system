package datastructures.graph;

import datastructures.hash.HashTable;

/**
 * Disjoint Set (Union-Find) with union-by-rank and path compression.
 * Used by Kruskal's algorithm to detect cycles.
 */
public class DisjointSet {

    private final HashTable<Integer, Integer> parent;
    private final HashTable<Integer, Integer> rank;

    public DisjointSet() {
        parent = new HashTable<>();
        rank   = new HashTable<>();
    }

    public void makeSet(int x) {
        parent.put(x, x);
        rank.put(x, 0);
    }

    /** Find with path compression. */
    public int find(int x) {
        if (parent.get(x) != x)
            parent.put(x, find(parent.get(x)));
        return parent.get(x);
    }

    /** Union by rank. */
    public void union(int x, int y) {
        int rx = find(x), ry = find(y);
        if (rx == ry) return;
        int rankX = rank.get(rx), rankY = rank.get(ry);
        if (rankX < rankY)      parent.put(rx, ry);
        else if (rankX > rankY) parent.put(ry, rx);
        else                  { parent.put(ry, rx); rank.put(rx, rankX + 1); }
    }

    public boolean connected(int x, int y) { return find(x) == find(y); }
}
