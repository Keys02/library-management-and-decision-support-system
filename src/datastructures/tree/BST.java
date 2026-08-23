package datastructures.tree;

import datastructures.linear.DynamicArray;

/**
 * Binary Search Tree generic over Comparable keys with associated values.
 * Supports insert, search, inorder traversal, delete.
 */
public class BST<K extends Comparable<K>, V> {

    private BSTNode<K, V> root;
    private int size;

    public BST() { root = null; size = 0; }

    // ── Insert ─────────────────────────────────────

    public void insert(K key, V value) {
        root = insertRec(root, key, value);
    }

    private BSTNode<K, V> insertRec(BSTNode<K, V> node, K key, V value) {
        if (node == null) { size++; return new BSTNode<>(key, value); }
        int cmp = key.compareTo(node.key);
        if      (cmp < 0) node.left  = insertRec(node.left,  key, value);
        else if (cmp > 0) node.right = insertRec(node.right, key, value);
        else              node.value = value;   // update existing key
        return node;
    }

    // ── Search ─────────────────────────────────────

    public V search(K key) {
        BSTNode<K, V> node = searchRec(root, key);
        return node == null ? null : node.value;
    }

    private BSTNode<K, V> searchRec(BSTNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if      (cmp < 0) return searchRec(node.left,  key);
        else if (cmp > 0) return searchRec(node.right, key);
        return node;
    }

    public boolean contains(K key) { return search(key) != null; }

    // ── Delete ─────────────────────────────────────

    public void delete(K key) { root = deleteRec(root, key); }

    private BSTNode<K, V> deleteRec(BSTNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if      (cmp < 0) node.left  = deleteRec(node.left,  key);
        else if (cmp > 0) node.right = deleteRec(node.right, key);
        else {
            size--;
            if (node.left  == null) return node.right;
            if (node.right == null) return node.left;
            // Replace with inorder successor (smallest in right subtree)
            BSTNode<K, V> successor = minNode(node.right);
            node.key   = successor.key;
            node.value = successor.value;
            node.right = deleteRec(node.right, successor.key);
            size++;   // compensate for double-decrement
        }
        return node;
    }

    private BSTNode<K, V> minNode(BSTNode<K, V> node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ── Traversals ─────────────────────────────────

    /** Returns keys in sorted (inorder) order. */
    public DynamicArray<K> inorder() {
        DynamicArray<K> result = new DynamicArray<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(BSTNode<K, V> node, DynamicArray<K> result) {
        if (node == null) return;
        inorderRec(node.left, result);
        result.add(node.key);
        inorderRec(node.right, result);
    }

    public int size()       { return size; }
    public boolean isEmpty(){ return size == 0; }
    public int height()     { return heightRec(root); }

    private int heightRec(BSTNode<K, V> node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }
}
