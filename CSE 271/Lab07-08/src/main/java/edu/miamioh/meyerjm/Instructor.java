// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

import java.util.ArrayList;

/**
 * Represents an instructor
 */
public class Instructor extends Person {
    // Define instance variables
    private int salary;
    private ArrayList<Course> listOfCourses;

    /**
     * Creates an Instructor object
     * @param uniqueIdentifier His ID
     * @param emailAddress His email address
     * @param salary His salary (in US pennies/year)
     */
    public Instructor(String uniqueIdentifier, String emailAddress, int salary) {
        super(uniqueIdentifier, emailAddress);
        this.salary = salary;
        this.listOfCourses = new ArrayList<Course>();
    } // end constructor Instructor

    /**
     * Gets this Instructor's list of courses
     * @return His list of courses
     */
    public ArrayList<Course> getListOfCourses() {
        return listOfCourses;
    } // end method getListOfCourses

    /**
     * Adds a course to this Instructor's list of classes to teach
     * @param course The Course
     */
    public void addCourse(Course course) {
        this.listOfCourses.add(course);
    } // end method addCourse

    /**
     * Gets this Instructor's salary
     * @return His salary
     */
    public int getSalary() {
        return salary;
    } // end method getSalary

    /**
     * Sets the given Student's gpa
     * @param gpa The gpa
     * @param student The student
     */
    public void assignGpa(double gpa, Student student) {
        student.setGpa(gpa);
    } // end method assignGpa

    /**
     * Gets a String representation of this Instructor
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Instructor{" +
            "salary=" + salary +
            "} " + super.toString();
    } // end method toString
} // end class Instructor
