/**
 * Created by John Meyer on 3/2/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab06
 */
public class MovablePoint implements Movable {
    // Define instance variables
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    /**
     * Creates a MovablePoint object
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @param xSpeed The x component of the speed with which the point is to move
     * @param ySpeed The y component of the speed with which the point is to move
     */
    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    } // end constructor MovablePoint

    // Define getters and setters
    public int getXSpeed() {
        return xSpeed;
    } // end method getXSpeed

    public void setXSpeed(int xSpeed) {
        if (xSpeed >= 0) {
            this.xSpeed = xSpeed;
        }
    } // end method setXSpeed

    public int getYSpeed() {
        return ySpeed;
    } // end method getYSpeed

    public void setYSpeed(int ySpeed) {
        if (ySpeed >= 0) {
            this.ySpeed = ySpeed;
        }
    } // end method setYSpeed

    /**
     * Converts this object to a String representation
     * @return The String representation
     */
    @Override
    public String toString() {
        return "MovablePoint{" +
                "x=" + x +
                ", y=" + y +
                ", xSpeed=" + xSpeed +
                ", ySpeed=" + ySpeed +
                '}';
    } // end method toString

    /**
     * Moves the object up (in the positive y direction)
     */
    public void moveUp() {
        y += ySpeed;
    } // end method moveUp

    /**
     * Moves the object down (in the negative y direction)
     */
    public void moveDown() {
        y -= ySpeed;
    } // end method moveDown

    /**
     * Moves the object left (in the negative x direction)
     */
    public void moveLeft() {
        x -= xSpeed;
    } // end method moveLeft

    /**
     * Moves the object right (in the positive x direction)
     */
    public void moveRight() {
        x += xSpeed;
    } // end method moveRight
} // end class MovablePoint
