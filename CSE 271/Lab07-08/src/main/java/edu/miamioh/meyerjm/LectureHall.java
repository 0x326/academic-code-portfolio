// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

/**
 * Represents a lecture hall
 */
public class LectureHall {
    // Define instance variables
    private int capacity;
    private String name;

    /**
     * Creates a LectureHall object.
     * @param capacity The room's capacity (in persons)
     * @param name The room's name
     */
    public LectureHall(int capacity, String name) {
        this.capacity = capacity;
        this.name = name;
    } // end constructor LectureHall

    /**
     * Gets this LectureHall's capacity
     * @return The capacity
     */
    public int getCapacity() {
        return capacity;
    } // end method getCapacity

    /**
     * Gets this LectureHall's name
     * @return The name
     */
    public String getName() {
        return name;
    } // end method getName

    /**
     * Returns the name of this LectureHall
     * @return The name
     */
    @Override
    public String toString() {
        return name;
    } // end method toString
} // end class LectureHall
