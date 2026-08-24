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

    public void preorder() {
        preorder(root);
    }

    private void preorder(BSTNode<T> node) {

        if (node == null)
            return;

        System.out.println(node.getData());

        preorder(node.getLeft());

        preorder(node.getRight());
    }

    public void postorder() {
        postorder(root);
    }

    private void postorder(BSTNode<T> node) {

        if (node == null)
            return;

        postorder(node.getLeft());

        postorder(node.getRight());

        System.out.println(node.getData());
    }

    public BSTNode<T> getRoot() {
        return root;
    }

    public void delete(T data) {
        root = delete(root, data);
    }

    private BSTNode<T> findMin(BSTNode<T> node) {

        while (node.getLeft() != null) {
            node = node.getLeft();
        }

        return node;
    }

    private BSTNode<T> delete(BSTNode<T> node, T data) {

        if (node == null) {
            return null;
        }

        int comparison = data.compareTo(node.getData());

        if (comparison < 0) {

            node.setLeft(delete(node.getLeft(), data));

        } else if (comparison > 0) {

            node.setRight(delete(node.getRight(), data));

        } else {

            // Case 1: No left child
            if (node.getLeft() == null) {
                return node.getRight();
            }

            // Case 2: No right child
            if (node.getRight() == null) {
                return node.getLeft();
            }

            // Case 3: Two children
            BSTNode<T> successor = findMin(node.getRight());

            node.setData(successor.getData());

            node.setRight(delete(node.getRight(), successor.getData()));
        }

        return node;
    }




}