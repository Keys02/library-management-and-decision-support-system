package test.graph;

import algorithms.graph.Prim;
import algorithms.graph.WeightedEdge;
import datastructures.graph.Graph;
import datastructures.linear.DynamicArray;

public class PrimTest {

    public static void main(String[] args) {

        Graph<String> graph = new Graph<>();

        graph.addVertex("Balme Library");
        graph.addVertex("Commonwealth Hall");
        graph.addVertex("Legon Hall");
        graph.addVertex("Business School");

        graph.addUndirectedEdge(0,1,2.5);
        graph.addUndirectedEdge(0,2,4.1);
        graph.addUndirectedEdge(1,3,1.2);
        graph.addUndirectedEdge(2,3,5.0);

        Prim<String> prim = new Prim<>();

        DynamicArray<WeightedEdge> mst =
                prim.minimumSpanningTree(graph,0);

        System.out.println("Prim MST");

        for(int i=0;i<mst.size();i++){

            System.out.println(mst.get(i));

        }

    }

}