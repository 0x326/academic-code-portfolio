import java.util.ArrayList;
import java.util.List;

// Project 08 - Speedy Lookups
// Course: CSE 274 A
// Professor: Dr. Gani

/**
 * A binary search tree.
 * <p>
 * Duplicates are not permitted.
 *
 * @author John Meyer
 */
public class BinarySearchTree<T extends Comparable> {

    private Node<T> root = null;
    private int numberOfNodes = 0;

    /**
     * @return The number of nodes in this tree.
     */
    public int size() {
        return numberOfNodes;
    }

    /**
     * @return Whether this tree is empty
     */
    public boolean isEmpty() {
        return numberOfNodes == 0;
    }

    /**
     * Adds the data to this tree
     * <p>
     * Note: Duplicates are not permitted
     * <p>
     * O(log N)
     *
     * @param data The data to add
     * @return Whether the data is added
     */
    public boolean add(T data) {
        if (root == null) {
            root = new Node<>(data);
            return true;
        }

        // Find location for new item
        Node<T> parentNode = null;
        Node<T> childNode = root;
        while (childNode != null) {
            if (data.compareTo(childNode.data) < 0) {
                // Go to the left
                parentNode = childNode;
                childNode = childNode.left;
            } else if (data.compareTo(childNode.data) > 0) {
                // Go to the right
                parentNode = childNode;
                childNode = childNode.right;
            } else {
                // Duplicates are not permitted
                return false;
            }
        }

        if (data.compareTo(parentNode.data) < 0) {
            // Place in left subtree
            parentNode.left = new Node<>(data);
        } else if (data.compareTo(parentNode.data) > 0) {
            // Place in right subtree
            parentNode.right = new Node<>(data);
        }
        return true;
    }

    private static <T extends Comparable> int countChildren(Node<T> node) {
        if (node.left != null && node.right != null) {
            return 2;
        } else if (node.left != null || node.right != null) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * An interface for lambda functions used by setChildToNull.
     */
    private interface ChildTest<T extends Comparable> {
        boolean test(Node<T> child);
    }

    /**
     * The first child of the parent that passes the test is set to null
     *
     * @param parent The parent node
     * @param childTest The test to impose on the children
     * @return Whether a child passes the test
     */
    private static <T extends Comparable> boolean setChildToNull(Node<T> parent, ChildTest<T> childTest) {
        if (parent == null) {
            return false;
        } else if (childTest.test(parent.left)) {
            parent.left = null;
        } else if (childTest.test(parent.right)) {
            parent.right = null;
        } else {
            return false;
        }
        return true;
    }

    /**
     * Removes the root of a given subtree.
     *
     * @param subtreeRoot The root of the subtree
     * @param subtreeParent The parent of the subtree root
     * @return Whether the root has been successfully removed
     * @throws IllegalStateException Thrown when the parent must remove its reference to the subtree root
     * but the parent is null (This happens in the case when the absolute root is the only node and it is being removed)
     */
    private static <T extends Comparable> boolean remove(Node<T> subtreeRoot, Node<T> subtreeParent) throws IllegalStateException {
        if (subtreeRoot == null) {
            return false;
        }

        int numberOfChildren = countChildren(subtreeRoot);
        if (numberOfChildren == 0) {
            // Remove subtreeRoot

            // Find whether subtreeRoot is in the left or the right
            if (subtreeParent == null) {
                throw new IllegalStateException();
            } else if (setChildToNull(subtreeParent, (child) -> child == subtreeRoot)) {
                // If block is entered, subtreeRoot is already removed
            } else {
                throw new IllegalArgumentException("subtreeParent is not a parent to subtreeRoot");
            }
        } else if (numberOfChildren == 1) {
            // Copy child data into subtreeRoot and delete child
            if (subtreeRoot.left != null) {
                subtreeRoot.data = subtreeRoot.left.data;
                subtreeRoot.right = subtreeRoot.left.right;
                subtreeRoot.left = subtreeRoot.left.left;
            } else {
                subtreeRoot.data = subtreeRoot.right.data;
                subtreeRoot.left = subtreeRoot.right.left;
                subtreeRoot.right = subtreeRoot.right.right;
            }
        } else {
            // Find the largest node that is smaller than subtreeRoot
            Node<T> maximumSmallerNodeParent = null;
            Node<T> maximumSmallerNode = subtreeRoot.left;
            while (maximumSmallerNode.right != null) {
                maximumSmallerNodeParent = maximumSmallerNode;
                maximumSmallerNode = maximumSmallerNode.right;
            }

            // Copy largest maximumSmallerNode data into subtreeRoot
            subtreeRoot.data = maximumSmallerNode.data;
            // Delete subtreeRoot
            if (maximumSmallerNodeParent != null) {
                return remove(maximumSmallerNode, maximumSmallerNodeParent);
            } else {
                // Parent is actually subtreeRoot
                return remove(maximumSmallerNode, subtreeRoot);
            }
        }
        return true;
    }

    /**
     * Finds the parent of the node containing the given value
     *
     * @param searchValue The value to look for
     * @param root The root of the tree (or subtree)
     * @return The parent node (or null if the node cannot be found)
     */
    private static <T extends Comparable> Node<T> findParent(T searchValue, Node<T> root) {
        if (searchValue == null) {
            throw new NullPointerException();
        } else if (root == null) {
            return null;
        } else if ((root.left != null && searchValue.equals(root.left.data)) ||
            (root.right != null && searchValue.equals(root.right.data))) {
            // root is a parent to the node we are looking for
            return root;
        } else {
            // Search the left subtree
            Node<T> node = findParent(searchValue, root.left);
            if (node == null) {
                // Search the right subtree
                node = findParent(searchValue, root.right);
            }

            // Return the node
            return node;
        }
    }

    /**
     * Removes the given value from this Tree.
     * <p>
     * O(log N)
     *
     * @param value The value to remove
     * @return Whether the value was successfully removed
     * (false in the case when it did not exist)
     */
    public boolean remove(T value) {
        if (root == null) {
            // Empty trees have nothing to remove
            return false;
        } else if (value.equals(root.data)) {
            try {
                return remove(root, null);
            } catch (IllegalStateException error) {
                // We cannot remove root by copying child data
                root = null;
                return true;
            }
        } else {
            // Find the subtree that parents the node we are removing
            Node<T> subtree = findParent(value, root);
            if (subtree == null) {
                // Cannot be found
                return false;
            } else if (subtree.left != null && value.equals(subtree.left.data)) {
                // The node we are removing is the left child
                return remove(subtree.left, subtree);
            } else {
                // The node we are removing is the right child
                return remove(subtree.right, subtree);
            }
        }
    }

    private enum TraversalMode {
        PREORDER,
        INORDER,
        POSTORDER
    }

    private List<T> traverseTree(Node<T> node, TraversalMode mode) {
        ArrayList<T> arrayList = new ArrayList<>();

        if (node != null) {
            if (mode == TraversalMode.PREORDER) {
                arrayList.add(node.data);
            }
            arrayList.addAll(traverseTree(node.left, mode));
            if (mode == TraversalMode.INORDER) {
                arrayList.add(node.data);
            }
            arrayList.addAll(traverseTree(node.right, mode));
            if (mode == TraversalMode.POSTORDER) {
                arrayList.add(node.data);
            }
        }

        return arrayList;
    }

    /**
     * Traverses this tree using inorder traversal into a List
     * <p>
     * O(N)
     *
     * @return A list of nodes arranged in traversal order.
     */
    public List<T> inorderTraversal() {
        return traverseTree(root, TraversalMode.INORDER);
    }

    private static class Node<T extends Comparable> {
        T data;
        Node<T> left;
        Node<T> right;

        public Node(T data) {
            this.data = data;
        }
    }
}
