package test.disjointset;

import datastructures.disjointset.DisjointSet;

public class DisjointSetTest {

    public static void main(String[] args) {

        DisjointSet ds = new DisjointSet(5);

        ds.union(0, 1);
        ds.union(1, 2);

        System.out.println(ds.connected(0, 2));

        System.out.println(ds.connected(0, 4));
    }
}