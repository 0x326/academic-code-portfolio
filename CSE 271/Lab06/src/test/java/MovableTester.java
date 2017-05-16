/**
 * Created by John Meyer on 3/2/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab06
 */
public class MovableTester {
    public static void main(String[] args) {
        // Begin testing point interface
        Movable m1 = new MovablePoint(5, 6, 10,12); // upcast
        System.out.println(m1);
        m1.moveLeft();
        System.out.println(m1);

        // Begin testing circle interface
        // upcast. Constructor takes in 4 point values and radius
        Movable m2 = new MovableCircle(2, 1, 2, 20,50);
        System.out.println(m2);
        m2.moveRight();
        System.out.println(m2);

        // Test cha cha slide
        Movable m3 = new MovablePoint(0, 0, 3, 12);
        System.out.println("== Cha Cha Slide ==");
        Movable[] objects = {m1, m2, m3};
        MovableCircle.chaChaSlide(objects);
        for (Movable obj : objects) {
            System.out.println(obj);
        }
    } // end method main
} // end class MovableTester
