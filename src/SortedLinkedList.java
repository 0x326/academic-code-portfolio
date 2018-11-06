import java.util.Objects;

/**
 * Course: CSE 274 F
 * Instructor: Dr. Md Osman Gani
 * <p>
 * Project 03
 *
 * @author John Meyer
 */
public class SortedLinkedList<T extends Comparable> {
    private Node<T> head = new Node<>(null);
    private int size = 0;
    private boolean duplicatesPermitted;

    public SortedLinkedList(boolean duplicatesPermitted) {
        this.duplicatesPermitted = duplicatesPermitted;
    }

    /**
     * Gets the current number of entries in this set.
     *
     * @return The integer number of entries currently in the set.
     */
    public int size() {
        return size;
    }

    /**
     * Sees whether this set is empty.
     *
     * @return True if the set is empty, or false if not.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds a new entry to this set, avoiding duplicates.
     *
     * @param value The integer to be added as a new entry.
     * @return True if the addition is successful, or false if the item already
     * is in the set.
     */
    public boolean add(T value) {
        if (!duplicatesPermitted && !contains(value)) {
            Node<T> nodeBeforeInsertionPoint = head;
            Node<T> nodeAfterInsertionPoint = head.next;
            while (nodeAfterInsertionPoint != null) {
                if (nodeAfterInsertionPoint.data.compareTo(value) < 0) {
                    break;
                }

                nodeBeforeInsertionPoint = nodeAfterInsertionPoint;
                nodeAfterInsertionPoint = nodeAfterInsertionPoint.next;
            }
            Node<T> newNode = new Node<>(value);

            // Insert node
            newNode.next = nodeAfterInsertionPoint;
            nodeBeforeInsertionPoint.next = newNode;
            size++;
            return true;
        }
        return false;
    }

    public T get(int index) {
        Node<T> currentNode = head.next;
        for (int i = 0; currentNode != null; i++) {
            if (i == index) {
                return currentNode.data;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    /**
     * Removes a specific entry from this set, if possible.
     *
     * @param value The entry to be removed.
     * @return True if the removal was successful, or false if not.
     */
    public boolean remove(T value) {
        // Find the node just before the removal point
        Node<T> nodeBeforeRemovalPoint = head;
        boolean valueIsFound = false;
        while (nodeBeforeRemovalPoint.next != null) {
            if (nodeBeforeRemovalPoint.next.data.equals(value)) {
                valueIsFound = true;
                break;
            } else {
                nodeBeforeRemovalPoint = nodeBeforeRemovalPoint.next;
            }
        }
        if (!valueIsFound) {
            return false;
        }

        Node<T> nodeToBeRemoved = nodeBeforeRemovalPoint.next;

        // Remove node
        nodeBeforeRemovalPoint.next = nodeToBeRemoved.next;
        size--;

        return true;
    }

    /**
     * Removes one unspecified entry from this set, if possible.
     *
     * @return Either the removed entry, if the removal was successful, or -1.
     */
    public T remove() {
        Node<T> nodeBeforeRemovalPoint = head;
        Node<T> nodeToBeRemoved = nodeBeforeRemovalPoint.next;

        // Remove node
        nodeBeforeRemovalPoint.next = nodeToBeRemoved.next;
        size--;

        return nodeToBeRemoved.data;
    }

    /**
     * Removes all entries from this set.
     */
    public void clear() {
        head.next = null;
        size = 0;
    }

    /**
     * Tests whether this set contains a given entry.
     *
     * @param value The entry to locate.
     * @return True if the set contains value, or false if not.
     */
    public boolean contains(T value) {
        Node<T> currentNode = head.next;
        // Search for node with the given value
        while (currentNode != null) {
            if (Objects.equals(currentNode.data, value)) {
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    /**
     * Retrieves all entries that are in this set.
     *
     * @return A newly allocated array of all the entries in the set, where the
     * size of the array is equal to the number of entries in the set.
     */
    public T[] toArray() {
        T[] array = (T[]) new Integer[size];
        Node<T> currentNode = head.next;
        for (int i = 0; currentNode != null; i++) {
            array[i] = currentNode.data;
            currentNode = currentNode.next;
        }
        return array;
    }

    /**
     * Returns a string representation of the items in the set,
     * specifically a space separated list of the strings, enclosed
     * in square brackets [].  For example, if the set contained
     * cat, dog, then this should return "[cat dog]".  If the
     * set were empty, then this should return "[]".
     *
     * @return A string representation of this set
     */
    public String toString() {
        StringBuilder representation = new StringBuilder("[");
        Node<T> currentNode = head.next;
        while (currentNode != null) {
            representation.append(currentNode.data).append(" ");
            currentNode = currentNode.next;
        }
        representation.deleteCharAt(representation.length() - 1);
        representation.append("]");
        return representation.toString();
    }

    private static class Node<T> {
        private T data;
        private Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }
}
