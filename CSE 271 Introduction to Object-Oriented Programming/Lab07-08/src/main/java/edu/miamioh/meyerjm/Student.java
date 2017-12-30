// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

/**
 * Represents a student
 */
public class Student extends Person {
    // Define instance variables
    private String major;
    private double gpa;

    /**
     * Creates a Student object. His gpa is initialized to -1.0
     * @param uniqueIdentifier His ID
     * @param emailAddress His email address
     * @param major His major
     */
    public Student(String uniqueIdentifier, String emailAddress, String major) {
        this(uniqueIdentifier, emailAddress, major, -1.0);
    } // end constructor Student

    /**
     * Creates a Student object
     * @param uniqueIdentifier His ID
     * @param emailAddress His email address
     * @param major His major
     * @param gpa His gpa; must be on a 4-point scale
     */
    public Student(String uniqueIdentifier, String emailAddress, String major, double gpa) {
        super(uniqueIdentifier, emailAddress);
        this.major = major;
        this.gpa = (gpa > 0 && gpa < 4.0) ? gpa : -1;
    } // end constructor Student

    /**
     * Get this Student's major
     * @return His major
     */
    public String getMajor() {
        return major;
    } // end method getMajor

    /**
     * Get this Student's GPA. -1.0 if he has not been assigned a gpa
     * @return His GPA
     */
    public double getGpa() {
        return gpa;
    } // end method getGpa

    /**
     * Sets this Student's GPA. Must be on a 4-point scale
     * @param gpa The gpa
     */
    public void setGpa(double gpa) {
        if (gpa > 0 && gpa <= 4.0) {
            this.gpa = gpa;
        }
    } // end method setGpa

    /**
     * Gets a String representation of this Student
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Student{" +
            "major='" + major + '\'' +
            ", gpa=" + gpa +
            "} " + super.toString();
    } // end method toString
} // end class Student
