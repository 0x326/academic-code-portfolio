// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

import java.util.ArrayList;

/**
 * Represents an academic course
 */
public class Course {
    // Define instance variables
    private String department;
    private String courseNumber;
    private String sectionLetter;
    private Instructor instructor;
    private LectureHall lectureHall;
    private ArrayList<Student> students;

    /**
     * Creates a Course object
     * @param department The department (such as "MUS")
     * @param courseNumber The course number (such as "100E"; can contain nonnumerical values)
     * @param sectionLetter The section letter (such as "A")
     */
    public Course(String department, String courseNumber, String sectionLetter) {
        this.department = department;
        this.courseNumber = courseNumber;
        this.sectionLetter = sectionLetter;
        this.instructor = null;
        this.lectureHall = null;
        this.students = new ArrayList<Student>();
    } // end constructor Course

    /**
     * Assigns the sole Instructor to this Course
     * @param instructor The Instructor to assign
     */
    public void setInstructor(Instructor instructor) {
        // Project Assumption: Only one instructor can teach a class
        this.instructor = instructor;
    } // end method setInstructor

    /**
     * Assigns the sole LectureHall to this Course
     * @param lectureHall The LectureHall to assign
     */
    public void setLectureHall(LectureHall lectureHall) {
        this.lectureHall = lectureHall;
    } // end method setLectureHall

    /**
     * Adds a Student to this Course. Avoids duplicates
     * @param student the Student to add
     */
    public void addStudent(Student student) {
        // Project Assumption: This course should admit a student even if it is beyond capacity to allow for "force-adds"
        if (!this.students.contains(student)) {
            this.students.add(student);
        }
    } // end method addStudent

    /**
     * Gets the list of Students in this Course
     * @return The list of Students
     */
    public ArrayList<Student> getStudents() {
        return students;
    } // end method getStudents

    /**
     * Returns the course number in the form DEP XXX-A by [Instructor] in [LectureHall] (X [of Y] seats filled).
     * Fields in brackets will be omitted or replaced by "null" if the field is unavailable
     * @return The string representation of this Course
     */
    @Override
    public String toString() {
        return department + " " + courseNumber + "-" + sectionLetter + " by " + instructor
            + " in " + lectureHall + " (" + students.size()
            + (lectureHall != null ? " of " + lectureHall.getCapacity() : "")
            + " seats filled)";
    } // end method toString
} // end class Course
