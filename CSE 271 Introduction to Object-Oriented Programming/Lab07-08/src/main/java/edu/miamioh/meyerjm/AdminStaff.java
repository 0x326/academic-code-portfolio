// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

/**
 * Represents an administrative staff-person
 */
public class AdminStaff extends Person {
    // Define instance variables
    private int salary;

    /**
     * Creates an AdminStaff object
     * @param uniqueIdentifier His ID
     * @param emailAddress His email address
     * @param salary His salary (in US pennies/year)
     */
    public AdminStaff(String uniqueIdentifier, String emailAddress, int salary) {
        super(uniqueIdentifier, emailAddress);
        this.salary = salary;
    } // end constructor AdminStaff

    /**
     * Gets this AdminStaff's salary
     * @return His salary
     */
    public int getSalary() {
        return salary;
    } // end method getSalary

    /**
     * Enrolls a Student in a Course
     * @param student The student
     * @param course The course
     */
    public void enroll(Student student, Course course) {
        course.addStudent(student);
    } // end method enroll

    /**
     * Assigns a LectureHall to a Course
     * @param course The course
     * @param lectureHall The lecture hall
     */
    public void assign(Course course, LectureHall lectureHall) {
        course.setLectureHall(lectureHall);
    } // end method assign

    /**
     * Assigns an Instructor to a Course
     * @param course The course
     * @param instructor The instructor
     */
    public void assign(Course course, Instructor instructor) {
        course.setInstructor(instructor);
        instructor.addCourse(course);
    } // end method assign

    /**
     * Gets a string representation of this AdminStaff
     * @return The string representation
     */
    @Override
    public String toString() {
        return "AdminStaff{" +
            "salary=" + salary +
            "} " + super.toString();
    } // end method toString
} // end class AdminStaff
