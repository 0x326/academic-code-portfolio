import java.util.ArrayList;

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
public class Stack<V> {
    private ArrayList<V> items;

    public Stack() {
        this(10);
    }

    public Stack(int initialCapacity) {
        items = new ArrayList<>(initialCapacity);
    }

    public void push(V item) {
        items.add(item);
    }

    public V pop() {
        return items.remove(items.size() - 1);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
