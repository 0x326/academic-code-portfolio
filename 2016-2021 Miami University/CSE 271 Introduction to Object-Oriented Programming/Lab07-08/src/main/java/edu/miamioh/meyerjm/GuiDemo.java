// Created by John Meyer on 3/25/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

import javax.swing.*;
import java.util.ArrayList;

/**
 * A simple GUI demo
 */
public class GuiDemo {
    // Define variables
    private static final University demoUniversity = new University();

    // Define sample data variables
    private static String[] sampleInstructorStringArguments = {
        "bravosad", "bravosad@miamioh.edu",
        "Bo.Brinkman", "Bo.Brinkman@miamioh.edu",
        "krumpenj", "krumpenj@MiamiOH.edu"
    };
    private static int[] sampleInstructorIntArguments = {
        5000000,
        5000001,
        4999999
    };
    private static String[] sampleStudentStringArguments = {
        "meyerjm", "meyerjm@miamioh.edu", "Computer Engineering",
        "doejohn", "doejohn@miamioh.edu", "Undecided",
        "doejane", "doejane@miamioh.edu", "General Engineering"
    };
    private static String[] sampleAdminStaffStringArguments = {
        "kirkmebp", "kirkmebp@miamioh.edu"
    };
    private static int[] sampleAdminStaffIntArguments = {
        7000000
    };
    private static String[] sampleCourseStringArguments = {
        "MUS", "100E", "A",
        "PHY", "192", "B",
        "PHY", "191", "C",
        "MTH", "249", "D",
        "ENG", "111", "NC",
        "MTH", "251", "E",
        "CSE", "174", "A",
        "CSE", "271", "B",
        "CSE", "102", "A"
    };
    private static int[] sampleLectureHallIntArguments = {
        100,
        20,
        200
    };
    private static String[] sampleLectureHallStringArguments = {
        "BEN 102",
        "BAC 255",
        "KRG 311"
    };

    /**
     * Fills a university with test objects
     * @param testUniversity The uinversity to fill
     */
    private static void fillUniversity(University testUniversity) {
        // Create test objects
        ArrayList<Instructor> testInstructors = new ArrayList<Instructor>();
        for (int i = 1; i < sampleInstructorStringArguments.length; i += 2) {
            testInstructors.add(new Instructor(sampleInstructorStringArguments[i - 1], sampleInstructorStringArguments[i], sampleInstructorIntArguments[i / 2]));
        }
        ArrayList<AdminStaff> testAdmins = new ArrayList<AdminStaff>();
        for (int i = 1; i < sampleAdminStaffStringArguments.length; i += 2) {
            testAdmins.add(new AdminStaff(sampleAdminStaffStringArguments[i - 1], sampleAdminStaffStringArguments[i], sampleAdminStaffIntArguments[i / 2]));
        }
        ArrayList<Student> testStudents = new ArrayList<Student>();
        for (int i = 2; i < sampleStudentStringArguments.length; i += 3) {
            testStudents.add(new Student(sampleStudentStringArguments[i - 2], sampleStudentStringArguments[i - 1], sampleStudentStringArguments[i]));
        }
        ArrayList<Course> testCourses = new ArrayList<Course>();
        for (int i = 2; i < sampleCourseStringArguments.length; i += 3) {
            testCourses.add(new Course(sampleCourseStringArguments[i - 2], sampleCourseStringArguments[i - 1], sampleCourseStringArguments[i]));
        }
        ArrayList<LectureHall> testLectureHalls = new ArrayList<LectureHall>();
        for (int i = 0; i < sampleLectureHallIntArguments.length; i++) {
            testLectureHalls.add(new LectureHall(sampleLectureHallIntArguments[i], sampleLectureHallStringArguments[i]));
        }

        for (Instructor instructor : testInstructors) {
            testUniversity.add(instructor);
        }
        for (Student student : testStudents) {
            testUniversity.add(student);
        }
        for (AdminStaff adminStaff : testAdmins) {
            testUniversity.add(adminStaff);
        }
        for (LectureHall lectureHall : testLectureHalls) {
            testUniversity.add(lectureHall);
        }
        for (Course course : testCourses) {
            testUniversity.add(course);
        }
    } // end method fillUniversity

    /**
     * Fills a University with sample data, displays its properties via a GUI,
     * and gives an opportunity to utilize Instructor and AdminStaff capabilities.
     * @param args Commandline arguements
     */
    public static void main(String[] args) {
        fillUniversity(demoUniversity);
        JFrame mainWindow = new StaffFunctionsFrame(demoUniversity);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setTitle("Lab07-08 GUI Demo");
        mainWindow.setVisible(true);
    } // end method main
} // end class GuiDemo
