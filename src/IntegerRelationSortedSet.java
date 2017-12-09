import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

// Project 08 - Speedy Lookups
// Course: CSE 274 A
// Professor: Dr. Gani

/**
 * Project 08 - Speedy Lookups
 * <p>
 * Course:
 * Instructor:
 *
 * @author John Meyer
 */
public class IntegerRelationSortedSet<T extends Comparable> {

    private BinarySearchTree<Integer> relatedIntegers = new BinarySearchTree<>();
    private Node<T>[] relation;
    private int size = 0;

    public IntegerRelationSortedSet() {
        this(20);
    }
    
    public IntegerRelationSortedSet(int initialCapacity) {
        relation = (Node<T>[]) new Node[initialCapacity];
    }

    public void clear() {
        relation = (Node<T>[]) new Node[relation.length];
        size = 0;
    }
    
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ensureCapacity(int capacity) {
        if (capacity >= relation.length) {
            // Array needs to be expanded
            relation = Arrays.copyOf(relation, 4 * relation.length / 3);
        }
    }

    public boolean add(int integer, T data) {
        ensureCapacity(integer);
        Node<T> node = relation[integer];

        if (node == null) {
            // The first node is missing (there is no relation here)
            relation[integer] = new Node<>(data);
            relatedIntegers.add(integer);
            return true;
        } else if (node.data.equals(data)) {
            // Node already exists
            return false;
        } else if (data.compareTo(node.data) < 0) {
            // Node is less than first node
            Node<T> newNode = new Node<>(data);
            newNode.next = node;
            relation[integer] = newNode;
            return true;
        }

        // Search for node (linked list is in sorted order)
        while (node.next != null && data.compareTo(node.next.data) < 0) {
            node = node.next;
        }

        if (node.next != null && node.next.data.equals(data)) {
            // Node already exists
            return false;
        } else {
            // Insert node
            Node<T> newNode = new Node<>(data);
            newNode.next = node.next;
            node.next = newNode;
            relatedIntegers.add(integer);
            return true;
        }
    }

    public boolean remove(int integer, T data) {
        Node<T> node = relation[integer];

        if (node == null) {
            // The first node is missing (there is no relation here)
            return false;
        } else if (node.data.equals(data)) {
            // Node exists
            // Remove node
            relation[integer] = node.next;
            if (node.next == null) {
                relatedIntegers.remove(integer);
            }
            return true;
        }

        // Search for node (linked list is in sorted order)
        while (node.next != null && data.compareTo(node.next.data) < 0) {
            node = node.next;
        }

        if (node.next != null && node.next.data.equals(data)) {
            // Node already exists
            // Remove node
            Node<T> removedNode = node.next;
            node.next = removedNode.next;
            return true;
        } else {
            // Node does not exist
            return false;
        }
    }

    public Iterator<Integer> iterator() {
        return null;
    }

    public List<T> toList() {
        ArrayList<T> relatedData = new ArrayList<>();
        List<Integer> integers = relatedIntegers.inorderTraversal();
        Stack<T> dataStack = new Stack<>();
        for (Integer integer : integers) {
            Node<T> dataNode = relation[integer];
            while (dataNode != null) {
                dataStack.push(dataNode.data);
                dataNode = dataNode.next;
            }

            while (!dataStack.isEmpty()) {
                relatedData.add(dataStack.pop());
            }
        }
        return relatedData;
    }

    private List<Integer> getRelatedIntegers() {
        return relatedIntegers.inorderTraversal();
    }

    /**
     * A data node
     *
     * @param <D> The type of the data
     */
    private static class Node<D extends Comparable> {
        D data;
        Node<D> next;

        private Node(D data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}
