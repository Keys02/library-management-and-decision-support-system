package test.tree;

import datastructures.tree.BinaryRedBlackTree;

public class RedBlackTreeTest {

    public static void main(String[] args) {

        BinaryRedBlackTree<Integer> tree =
                new BinaryRedBlackTree<>();

        tree.insert(10);
        tree.insert(20);
        tree.insert(30);
        tree.insert(15);
        tree.insert(25);
        tree.insert(5);

        System.out.println("Root: " + tree.getRoot().getData());
        System.out.println("Root is red? " + tree.getRoot().isRed());

        System.out.println("Root: "
                + tree.getRoot().getData());

        System.out.println("Root is red? "
                + tree.getRoot().isRed());

    }

}