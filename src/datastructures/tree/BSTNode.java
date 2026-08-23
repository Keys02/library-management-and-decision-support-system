package datastructures.tree;

class BSTNode<K, V> {
    K key;
    V value;
    BSTNode<K, V> left, right;

    BSTNode(K key, V value) { this.key = key; this.value = value; }
}
