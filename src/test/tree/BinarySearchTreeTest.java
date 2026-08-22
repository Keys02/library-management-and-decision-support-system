package test.tree;

import datastructures.tree.BinarySearchTree;

public class BinarySearchTreeTest {

    public static void main(String[] args) {

        BinarySearchTree<Integer> tree =
                new BinarySearchTree<>();

        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        System.out.println("Root: "
                + tree.getRoot().getData());

        System.out.println("Left: "
                + tree.getRoot().getLeft().getData());

        System.out.println("Right: "
                + tree.getRoot().getRight().getData());

        System.out.println();

        System.out.println("Inorder Traversal");

        tree.inorder();   
        
        System.out.println();

        System.out.println("Contains 60: "
                + tree.contains(60));

        System.out.println("Contains 35: "
                + tree.contains(35));

    }

}