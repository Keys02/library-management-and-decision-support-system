package datastructures.tree;

public class BinarySearchTree<T extends Comparable<T>> {

    private BSTNode<T> root;

    public BinarySearchTree() {
        root = null;
    }public void insert(T data) {
        root = insert(root, data);
    }

    private BSTNode<T> insert(BSTNode<T> node, T data) {

        if (node == null) {
            return new BSTNode<>(data);
        }

        if (data.compareTo(node.getData()) < 0) {

            node.setLeft(
                    insert(node.getLeft(), data)
            );

        } else if (data.compareTo(node.getData()) > 0) {

            node.setRight(
                    insert(node.getRight(), data)
            );

        }

        return node;
    }

    public BSTNode<T> getRoot() {
        return root;
    }

    public boolean contains(T data) {
        return contains(root, data);
    }  

    private boolean contains(BSTNode<T> node, T data) {

        if (node == null) {
            return false;
        }

        int comparison = data.compareTo(node.getData());

        if (comparison == 0) {
            return true;
        }

        if (comparison < 0) {
            return contains(node.getLeft(), data);
        }

        return contains(node.getRight(), data);
    }

    public void inorder() {
        inorder(root);
    }

    private void inorder(BSTNode<T> node) {

        if (node == null) {
            return;
        }

        inorder(node.getLeft());

        System.out.println(node.getData());

        inorder(node.getRight());

    }


}