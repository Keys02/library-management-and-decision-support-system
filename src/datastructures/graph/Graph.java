package datastructures.graph;

import model.Library;
import model.Road;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private final Map<Integer, Library> libraries;

    private final Map<Integer, List<Road>> adjacencyList;

    public Graph() {

        libraries = new HashMap<>();

        adjacencyList = new HashMap<>();

    }

}