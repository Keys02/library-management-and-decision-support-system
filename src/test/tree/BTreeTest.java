package test.tree;

import datastructures.tree.BTree;

public class BTreeTest {

   public static void main(String[] args) {

      BTree<Integer> tree = new BTree<>(3);

      tree.insert(10);
      tree.insert(20);
      tree.insert(5);
      tree.insert(6);
      tree.insert(12);
      tree.insert(30);
      tree.insert(7);
      tree.insert(17);

      System.out.println("Traversal:");

      tree.traverse();

  }

}