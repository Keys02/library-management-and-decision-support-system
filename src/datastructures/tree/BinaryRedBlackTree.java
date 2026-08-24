package datastructures.tree;

public class BinaryRedBlackTree<T extends Comparable<T>> {

    private RBNode<T> root;

    public BinaryRedBlackTree() {

        root = null;

    }

    public void insert(T data) {

        RBNode<T> node = new RBNode<>(data);

        root = insert(root, node);

        fixInsert(node);

        root.setRed(false);
    }

    private RBNode<T> insert(RBNode<T> current,
                            RBNode<T> newNode) {

        if (current == null) {
            return newNode;
        }

        if (newNode.getData().compareTo(current.getData()) < 0) {

            current.setLeft(insert(current.getLeft(), newNode));

            current.getLeft().setParent(current);

        } else if (newNode.getData().compareTo(current.getData()) > 0) {

            current.setRight(insert(current.getRight(), newNode));

            current.getRight().setParent(current);

        }

        return current;
    }

    public RBNode<T> getRoot() {
        return root;
    }

    private void leftRotate(RBNode<T> x) {

        RBNode<T> y = x.getRight();

        x.setRight(y.getLeft());

        if (y.getLeft() != null) {
            y.getLeft().setParent(x);
        }

        y.setParent(x.getParent());

        if (x.getParent() == null) {

            root = y;

        } else if (x == x.getParent().getLeft()) {

            x.getParent().setLeft(y);

        } else {

            x.getParent().setRight(y);

        }

        y.setLeft(x);

        x.setParent(y);
    }

    private void rightRotate(RBNode<T> y) {

        RBNode<T> x = y.getLeft();

        y.setLeft(x.getRight());

        if (x.getRight() != null) {
            x.getRight().setParent(y);
        }

        x.setParent(y.getParent());

        if (y.getParent() == null) {

            root = x;

        } else if (y == y.getParent().getLeft()) {

            y.getParent().setLeft(x);

        } else {

            y.getParent().setRight(x);

        }

        x.setRight(y);

        y.setParent(x);
    }

    private void fixInsert(RBNode<T> node) {

        while (node != root &&
            node.getParent() != null &&
            node.getParent().isRed()) {

            RBNode<T> parent = node.getParent();
            RBNode<T> grandparent = parent.getParent();

            if (grandparent == null) {
                break;
            }

            if (parent == grandparent.getLeft()) {

                RBNode<T> uncle = grandparent.getRight();

                // Case 1: Uncle is red
                if (uncle != null && uncle.isRed()) {

                    parent.setRed(false);
                    uncle.setRed(false);
                    grandparent.setRed(true);

                    node = grandparent;

                } else {

                    // Case 2: Triangle
                    if (node == parent.getRight()) {

                        node = parent;

                        leftRotate(node);

                        parent = node.getParent();
                        grandparent = parent.getParent();
                    }

                    // Case 3: Straight line
                    parent.setRed(false);
                    grandparent.setRed(true);

                    rightRotate(grandparent);

                }

            } else {

                // Mirror image

                RBNode<T> uncle = grandparent.getLeft();

                if (uncle != null && uncle.isRed()) {

                    parent.setRed(false);
                    uncle.setRed(false);
                    grandparent.setRed(true);

                    node = grandparent;

                } else {

                    if (node == parent.getLeft()) {

                        node = parent;

                        rightRotate(node);

                        parent = node.getParent();
                        grandparent = parent.getParent();
                    }

                    parent.setRed(false);
                    grandparent.setRed(true);

                    leftRotate(grandparent);

                }

            }

        }

        root.setRed(false);
    }
}