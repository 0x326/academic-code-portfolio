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

    public HashMap() {
        this(20);
    }

    public HashMap(Integer initialCapacity) {
        hashTable = (Node<K, V>[]) new Object[initialCapacity];
    }

    public int size() {
        return numberOfKeys;
    }

    public boolean isEmpty() {
        return numberOfKeys == 0;
    }

    public V get(Object key) {
        if (key == null) {
            return null;
        }

        int hastTableLocation = key.hashCode() % hashTable.length;
        Node<K, V> node = hashTable[hastTableLocation];
        while (node != null && !node.key.equals(key)) {
            node = node.next;
        }

        return node != null ? node.value : null;
    }

    public V put(K key, V value) {
        if (key == null) {
            return null;
        }

        int hastTableLocation = key.hashCode() % hashTable.length;
        Node<K, V> node = hashTable[hastTableLocation];

        if (node == null) {
            hashTable[hastTableLocation] = new Node<>(key, value);
            return null;
        }

        while (node.next != null && !node.key.equals(key)) {
            node = node.next;
        }

        if (node.key.equals(key)) {
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        } else {
            node.next = new Node<>(key, value);
            return null;
        }
    }

    public V remove(Object key) {
        if (key == null) {
            return null;
        }

        int hastTableLocation = key.hashCode() % hashTable.length;
        Node<K, V> node = hashTable[hastTableLocation];

        if (node == null) {
            return null;
        }

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
        hashTable = (Node<K, V>[]) new Object[hashTable.length];
        numberOfKeys = 0;
    }

    public V getOrDefault(Object key, V defaultValue) {
        V value = get(key);
        return value != null ? value : defaultValue;
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
