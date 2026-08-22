package datastructures.tree;

public class RBNode<T extends Comparable<T>> {

    private T data;

    private RBNode<T> left;

    private RBNode<T> right;

    private RBNode<T> parent;

    private boolean red;

    public RBNode(T data) {

        this.data = data;

        this.red = true;

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RBNode<T> getLeft() {
        return left;
    }

    public void setLeft(RBNode<T> left) {
        this.left = left;
    }

    public RBNode<T> getRight() {
        return right;
    }

    public void setRight(RBNode<T> right) {
        this.right = right;
    }

    public RBNode<T> getParent() {
        return parent;
    }

    public void setParent(RBNode<T> parent) {
        this.parent = parent;
    }

    public boolean isRed() {
        return red;
    }

    public void setRed(boolean red) {
        this.red = red;
    }

}