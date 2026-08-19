package datastructures.hash;

import datastructures.linear.LinkedList;

public class HashTable<K, V> {

    private static final int DEFAULT_CAPACITY = 16;

    private LinkedList<HashEntry<K, V>>[] table;

    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        table = (LinkedList<HashEntry<K, V>>[]) new LinkedList[DEFAULT_CAPACITY];

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            table[i] = new LinkedList<>();
        }

        size = 0;
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % table.length;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void put(K key, V value) {

        int index = hash(key);

        LinkedList<HashEntry<K, V>> bucket = table[index];

        for (int i = 0; i < bucket.size(); i++) {

            HashEntry<K, V> entry = bucket.get(i);

            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }

        bucket.addLast(new HashEntry<>(key, value));

        size++;
    }

    public V get(K key) {

        int index = hash(key);

        LinkedList<HashEntry<K, V>> bucket = table[index];

        for (int i = 0; i < bucket.size(); i++) {

            HashEntry<K, V> entry = bucket.get(i);

            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public V remove(K key) {

        int index = hash(key);

        LinkedList<HashEntry<K, V>> bucket = table[index];

        for (int i = 0; i < bucket.size(); i++) {

            HashEntry<K, V> entry = bucket.get(i);

            if (entry.getKey().equals(key)) {

                V value = entry.getValue();

                bucket.remove(i);

                size--;

                return value;
            }
        }

        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public double getLoadFactor() {
        return (double) size / table.length;
    }

    public int countCollisions() {

      int collisions = 0;

      for (LinkedList<HashEntry<K, V>> bucket : table) {

          if (bucket.size() > 1) {
              collisions += bucket.size() - 1;
          }

      }

      return collisions;
  }

}