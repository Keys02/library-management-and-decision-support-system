package test.graph;

import datastructures.graph.Graph;
import datastructures.linear.LinkedList;
import model.Library;
import model.Road;

public class PrimTest {

    public static void main(String[] args) {

        Graph graph = new Graph();

        // Add libraries
        graph.addLibrary(new Library(
                0,
                "Balme Library",
                "UG Main Campus",
                "8:00 AM - 10:00 PM"
        ));

        graph.addLibrary(new Library(
                1,
                "Commonwealth Hall",
                "UG",
                "8:00 AM - 10:00 PM"
        ));

        graph.addLibrary(new Library(
                2,
                "Legon Hall",
                "UG",
                "8:00 AM - 10:00 PM"
        ));

        graph.addLibrary(new Library(
                3,
                "Business School",
                "UG",
                "8:00 AM - 10:00 PM"
        ));

        // Add roads
        graph.addRoad(new Road(
                1, 0, 1, 2.5, 2.5
        ));

        graph.addRoad(new Road(
                2, 0, 2, 4.1, 4.1
        ));

        graph.addRoad(new Road(
                3, 1, 3, 1.2, 1.2
        ));

        graph.addRoad(new Road(
                4, 2, 3, 5.0, 5.0
        ));

        // Run Prim's algorithm starting from library 0
        LinkedList<Road> mst = graph.prim(0);

        System.out.println("Prim MST");

        for (int i = 0; i < mst.size(); i++) {
            System.out.println(mst.get(i));
        }
    }
}