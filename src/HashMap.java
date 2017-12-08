import java.util.ArrayList;
import java.util.Map;

/**
 * Project 08
 * <p>
 * Course: CSE 274 A
 * Instructor: Dr. Gani
 *
 * @author John Meyer
 */
public class HashMap<K, V> {

    private int numberOfKeys = 0;
    private Node<K, V>[] hashTable;
    private ArrayList<K> keys = new ArrayList<>();
    private final double maxLoadFactor = 0.60;

    public HashMap() {
        this(20);
    }

    public HashMap(int initialCapacity) {
        hashTable = (Node<K, V>[]) new Node[initialCapacity];
    }

    public int size() {
        return numberOfKeys;
    }

    public boolean isEmpty() {
        return numberOfKeys == 0;
    }

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

    public V put(K key, V value) {
        if ((double) numberOfKeys / hashTable.length > maxLoadFactor) {
            growHashTable();
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

    public V remove(Object key) {
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

    private void growHashTable() {
        int newSize = 2 * hashTable.length;
        Node<K, V>[] newHashTable = (Node<K, V>[]) new Node[newSize];

        for (K key : keys) {
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
