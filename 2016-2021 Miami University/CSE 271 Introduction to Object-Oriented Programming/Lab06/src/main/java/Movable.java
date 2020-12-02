/**
 * Created by John Meyer on 3/2/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab06
 */
public interface Movable {
    /**
     * Moves the object up (in the positive y direction)
     */
    void moveUp();

    /**
     * Moves the object down (in the negative y direction)
     */
    void moveDown();

    /**
     * Moves the object left (in the negative x direction)
     */
    void moveLeft();

    /**
     * Moves the object right (in the positive x direction)
     */
    void moveRight();
} // end interface Movable
