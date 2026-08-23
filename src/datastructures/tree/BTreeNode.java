package datastructures.tree;

public class BTreeNode<T extends Comparable<T>> {

    private int numberOfKeys;

    private boolean leaf;

    private Comparable<T>[] keys;

    private BTreeNode<T>[] children;

    @SuppressWarnings("unchecked")
    public BTreeNode(int degree, boolean leaf) {

        this.leaf = leaf;

        keys = new Comparable[2 * degree - 1];

        children = new BTreeNode[2 * degree];

        numberOfKeys = 0;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public int getNumberOfKeys() {
        return numberOfKeys;
    }

    public void setNumberOfKeys(int numberOfKeys) {
        this.numberOfKeys = numberOfKeys;
    }

    public Comparable<T>[] getKeys() {
        return keys;
    }

    public BTreeNode<T>[] getChildren() {
        return children;
    }
}