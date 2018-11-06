import java.util.ArrayList;
import java.util.Map;

// Project 08 - Speedy Lookups
// Course: CSE 274 A
// Professor: Dr. Gani

/**
 * A HashMap (dictionary) with linked-chaining
 *
 * @author John Meyer
 */
public class HashMap<K extends Comparable, V> {

    private int numberOfKeys = 0;
    private Node<K, V>[] hashTable;
    private SortedLinkedList<K> keys = new SortedLinkedList<>(false);
    /**
     * loadFactor = (entries)/(tableSize)
     */
    private final double maxLoadFactor = 0.60;

    /**
     * Creates a HashMap
     */
    public HashMap() {
        this(20);
    }

    /**
     * Creates a HashMap
     * @param initialCapacity Entries this dictionary can hold before its first resize
     */
    public HashMap(int initialCapacity) {
        hashTable = (Node<K, V>[]) new Node[(int) Math.ceil(initialCapacity / maxLoadFactor)];
    }

    /**
     * @return The number of keys
     */
    public int size() {
        return numberOfKeys;
    }

    /**
     * @return Whether this HashMap is empty
     */
    public boolean isEmpty() {
        return numberOfKeys == 0;
    }

    /**
     * Gets the value associated with the given key.
     * <p>
     * Average case: O(1)
     * Worst case: O(N)
     *
     * @param key The associated key
     * @return The associated value (or null if non-existent)
     */
    public V get(K key) {
        return get(key, hashTable);
    }

    private static <K, V> V get(K key, Node<K, V>[] hashTable) {
        if (key == null) {
            return null;
        }

        int hastTableLocation = Math.abs(computeHashCode(key) % hashTable.length);
        Node<K, V> node = hashTable[hastTableLocation];
        // Search for the right node (linked chaining)
        while (node != null && !node.key.equals(key)) {
            node = node.next;
        }

        // Return the data if it can be found
        return node != null ? node.value : null;
    }

    /**
     * Assigns the key to the given value.
     * <p>
     * Average case: O(1)
     * Worst case: O(N)
     *
     * @param key   The key to assign
     * @param value The value to associate
     * @return The old value, if applicable
     */
    public V put(K key, V value) {
        if ((double) numberOfKeys / hashTable.length > maxLoadFactor) {
            growHashTable();
        }

        if (!keys.contains(key)) {
            keys.add(key);
        }
        return put(key, value, hashTable);
    }

    private static <K, V> V put(K key, V value, Node<K, V>[] hashTable) {
        if (key == null) {
            return null;
        }

        int hastTableLocation = Math.abs(computeHashCode(key) % hashTable.length);
        Node<K, V> node = hashTable[hastTableLocation];

        if (node == null) {
            hashTable[hastTableLocation] = new Node<>(key, value);
            return null;
        }

        // Search for the right node (linked chaining)
        while (node.next != null && !node.key.equals(key)) {
            node = node.next;
        }

        if (node.key.equals(key)) {
            // Replace value in pre-existing node
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {
            // Create new node
            node.next = new Node<>(key, value);
            return null;
        }
    }

    /**
     * Removes the value associated with this key.
     * <p>
     * Average case: O(1)
     * Worst case: O(N)
     *
     * @param key The key of the value to disassociate
     * @return The disassociated value
     */
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int hastTableLocation = Math.abs(computeHashCode(key) % hashTable.length);
        Node<K, V> node = hashTable[hastTableLocation];

        if (node == null) {
            return null;
        }

        // Search for the right node (linked chaining)
        while (node.next != null && !node.next.key.equals(key)) {
            node = node.next;
        }

        if (node.next != null && node.next.key.equals(key)) {
            // Remove node
            V removedValue = node.next.value;
            node.next = node.next.next;

            keys.remove(key);
            return removedValue;
        } else {
            return null;
        }
    }

    public void clear() {
        hashTable = (Node<K, V>[]) new Node[hashTable.length];
        numberOfKeys = 0;
    }

    public V getOrDefault(K key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Rehashes the hash table.
     * <p>
     * O(N)
     */
    private void growHashTable() {
        int newSize = 2 * hashTable.length;
        Node<K, V>[] newHashTable = (Node<K, V>[]) new Node[newSize];

        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = get(key);
            put(key, value, newHashTable);
        }

        hashTable = newHashTable;
    }

    private static int computeHashCode(Object object) {
        if (object instanceof String) {
            int hashCode = 0;
            String string = (String) object;
            for (int i = 0; i < string.length(); i++) {
                hashCode += string.charAt(i) * Math.pow(31, string.length() - i - 1);
            }
            return hashCode;
        } else {
            return object.hashCode();
        }
    }

    public K[] keys() {
        return keys.toArray();
    }

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> next;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

}
