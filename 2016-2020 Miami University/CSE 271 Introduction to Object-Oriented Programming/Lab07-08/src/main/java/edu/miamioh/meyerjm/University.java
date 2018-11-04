// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

import java.util.ArrayList;

/**
 * Represents a university. Stores a collection of Person's and Course's
 */
public class University {
    // Define instance variables
    private ArrayList<Person> listOfAssociatedPersons;
    private ArrayList<Course> listOfCourses;
    private ArrayList<LectureHall> listOfLectureHalls;

    /**
     * Creates a University object
     */
    public University() {
        this.listOfAssociatedPersons = new ArrayList<Person>();
        this.listOfCourses = new ArrayList<Course>();
        this.listOfLectureHalls = new ArrayList<LectureHall>();
    } // end constructor University

    /**
     * Adds the given person to this University's list of employees/students. Used for organization purposes
     * @param person The person to add
     */
    public void add(Person person) {
        listOfAssociatedPersons.add(person);
    } // end method add

    /**
     * Adds the given LectureHall to this University. Used for organization purposes
     * @param lectureHall The LectureHall to add
     */
    public void add(LectureHall lectureHall) {
        listOfLectureHalls.add(lectureHall);
    } // end method add

    /**
     * Adds a given course to this University's list of courses. Used for organization purposes
     * @param course The course to add
     */
    public void add(Course course) {
        listOfCourses.add(course);
    } // end method add

    /**
     * Gets this University's list of courses
     * @return The list of courses
     */
    public ArrayList<Course> getCourses() {
        return listOfCourses;
    } // end method getCourses

    /**
     * Gets this University's list of lecture halls
     * @return The list of lecture halls
     */
    public ArrayList<LectureHall> getLectureHalls() {
        return listOfLectureHalls;
    } // end method getLectureHalls

    /**
     * Gets this University's list of employees/students
     * @return The list of employee's
     */
    public ArrayList<Person> getAllRelatedPeople() {
        return listOfAssociatedPersons;
    } // end method getAllRelatedPeople

    /**
     * Gets a list of this University's instructors
     * @return The list of instructors
     */
    public ArrayList<Instructor> getInstructors() {
        ArrayList<Instructor> listOfInstructors = new ArrayList<Instructor>();
        for (Person person : listOfAssociatedPersons) {
            if (person instanceof Instructor) {
                listOfInstructors.add((Instructor) person);
            }
        }
        return listOfInstructors;
    } // end method getInstructors

    /**
     * Gets a list of this University's admin staff
     * @return The list of admins
     */
    public ArrayList<AdminStaff> getAdmins() {
        ArrayList<AdminStaff> listOfAdmins = new ArrayList<AdminStaff>();
        for (Person person : listOfAssociatedPersons) {
            if (person instanceof AdminStaff) {
                listOfAdmins.add((AdminStaff) person);
            }
        }
        return listOfAdmins;
    } // end method getAdmins

    /**
     * Gets a list of this University's students
     * @return The list of students
     */
    public ArrayList<Student> getStudents() {
        ArrayList<Student> listOfStudents = new ArrayList<Student>();
        for (Person person : listOfAssociatedPersons) {
            if (person instanceof Student) {
                listOfStudents.add((Student) person);
            }
        }
        return listOfStudents;
    } // end method getStudents
} // end class University
