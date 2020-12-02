/**
 * Created by John Meyer on 3/2/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab06
 */
public class MovableCircle implements Movable {
    // Define instance variables
    private MovablePoint center;
    private int radius;

    /**
     * Creates a MovableCircle object
     * @param centerX The x coordinate of the center
     * @param centerY The y coordinate of the center
     * @param xSpeed The x component of the speed with which it is to move
     * @param ySpeed The y component of the speed with which it is to move
     * @param radius The radius of the circle
     */
    public MovableCircle(int centerX, int centerY, int xSpeed, int ySpeed, int radius) {
        this.center = new MovablePoint(centerX, centerY, xSpeed, ySpeed);
        this.radius = radius;
    } // end constructor MovableCircle

    /**
     * Moves the object up (in the positive y direction)
     */
    public void moveUp() {
        center.moveUp();
    } // end method moveUp

    /**
     * Moves the object down (in the negative y direction)
     */
    public void moveDown() {
        center.moveDown();
    } // end method moveDown

    /**
     * Moves the object left (in the negative x direction)
     */
    public void moveLeft() {
        center.moveLeft();
    } // end method moveLeft

    /**
     * Moves the object right (in the positive x direction)
     */
    public void moveRight() {
        center.moveRight();
    } // end method moveRight

    /**
     * Create a String representation of the MovableCircle
     * @return The String representation
     */
    @Override
    public String toString() {
        return "MovableCircle{" +
                "center=" + center +
                ", radius=" + radius +
                '}';
    } // end method toString

    /**
     * Move objects to the cha cha slide
     * @param objects The
     */
    public static void chaChaSlide(Movable[] objects) {
        // Move each object
        for (Movable obj : objects) {
            obj.moveLeft();
            obj.moveRight();
            obj.moveDown();
            obj.moveUp();

            obj.moveLeft();
            obj.moveDown();
            obj.moveUp();
            obj.moveRight();

            obj.moveLeft();
            obj.moveDown();
            obj.moveUp();
            obj.moveUp();
        }
    } // end method chaChaSlide
} // end class MovableCircle
