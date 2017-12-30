// Created by John Meyer on 3/22/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

import java.util.ArrayList;

/**
 * Tests the classes within this package
 */
public class Tester {
    // Define testing variables
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
    private static ArrayList<Instructor> testInstructors = null;
    private static ArrayList<AdminStaff> testAdmins = null;
    private static ArrayList<Student> testStudents = null;
    private static ArrayList<Course> testCourses = null;
    private static ArrayList<LectureHall> testLectureHalls = null;

    /**
     * Fills a university with test objects
     * @param testUniversity The uinversity to fill
     */
    private static void fillUniversity(University testUniversity) {
        // Create test objects
        testInstructors = new ArrayList<Instructor>();
        for (int i = 1; i < sampleInstructorStringArguments.length; i += 2) {
            testInstructors.add(new Instructor(sampleInstructorStringArguments[i - 1], sampleInstructorStringArguments[i], sampleInstructorIntArguments[i / 2]));
        }
        testAdmins = new ArrayList<AdminStaff>();
        for (int i = 1; i < sampleAdminStaffStringArguments.length; i += 2) {
            testAdmins.add(new AdminStaff(sampleAdminStaffStringArguments[i - 1], sampleAdminStaffStringArguments[i], sampleAdminStaffIntArguments[i / 2]));
        }
        testStudents = new ArrayList<Student>();
        for (int i = 2; i < sampleStudentStringArguments.length; i += 3) {
            testStudents.add(new Student(sampleStudentStringArguments[i - 2], sampleStudentStringArguments[i - 1], sampleStudentStringArguments[i]));
        }
        testCourses = new ArrayList<Course>();
        for (int i = 2; i < sampleCourseStringArguments.length; i += 3) {
            testCourses.add(new Course(sampleCourseStringArguments[i - 2], sampleCourseStringArguments[i - 1], sampleCourseStringArguments[i]));
        }
        testLectureHalls = new ArrayList<LectureHall>();
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
     * Tests adding to a University and retrieval from it
     */
    private static void testOrganization() {
        University testUniversity = new University();
        fillUniversity(testUniversity);
        System.out.println("Comparing Instructors");
        System.out.println("Expected: " + testInstructors);
        System.out.println("Actual:   " + testUniversity.getInstructors());
        System.out.println();

        System.out.println("Comparing AdminStaff");
        System.out.println("Expected: " + testAdmins);
        System.out.println("Actual:   " + testUniversity.getAdmins());
        System.out.println();

        System.out.println("Comparing Students");
        System.out.println("Expected: " + testStudents);
        System.out.println("Actual:   " + testUniversity.getStudents());
        System.out.println();

        System.out.println("Comparing Courses");
        System.out.println("Expected: " + testCourses);
        System.out.println("Actual:   " + testUniversity.getCourses());
        System.out.println();

        System.out.println("Comparing LectureHalls");
        System.out.println("Expected: " + testLectureHalls);
        System.out.println("Actual:   " + testUniversity.getLectureHalls());
        System.out.println();

        ArrayList<Person> allTestPeople = new ArrayList<Person>();
        for (Instructor instructor : testInstructors) {
            allTestPeople.add(instructor);
        }
        for (AdminStaff adminStaff : testAdmins) {
            allTestPeople.add(adminStaff);
        }
        for (Student student : testStudents) {
            allTestPeople.add(student);
        }
        allTestPeople.sort(null);
        ArrayList<Person> recordedPeople = testUniversity.getAllRelatedPeople();
        recordedPeople.sort(null);

        System.out.println("Comparing All Related People");
        System.out.println("Expected: " + allTestPeople);
        System.out.println("Actual:   " + recordedPeople);
        System.out.println();
    } // end method testOrganization

    /**
     * Tests the capabilities of AdminStaff and Instructor objects
     */
    private static void testAcademics() {
        University testUniversity = new University();
        fillUniversity(testUniversity);

        // Grab a sample advisor
        AdminStaff testAdvisor = testUniversity.getAdmins().get(0);
        int numOfLectureHalls = testUniversity.getLectureHalls().size();
        int numOfInstructors = testUniversity.getInstructors().size();
        int numOfStudents = testUniversity.getStudents().size();
        int[] numOfCoursesAssignedToEachInstructor = new int[numOfInstructors];

        for (int i = 0; i < testUniversity.getCourses().size(); i++) {
            System.out.println("Filling Course at index " + i);

            // Get items to add
            Course course = testUniversity.getCourses().get(i);
            LectureHall lectureHall = testUniversity.getLectureHalls().get(i % numOfLectureHalls);
            Instructor instructor = testUniversity.getInstructors().get(i % numOfInstructors);
            Student student = testUniversity.getStudents().get(i % numOfStudents);

            // Add items
            testAdvisor.assign(course, lectureHall);
            testAdvisor.assign(course, instructor);
            testAdvisor.enroll(student, course);

            // Verify items are added
            System.out.println("Comparing AdminStaff and Course functionality");
            String expectedToStringResult = sampleCourseStringArguments[3 * i] + " "
                + sampleCourseStringArguments[3 * i + 1] + "-" + sampleCourseStringArguments[3 * i + 2]
                + " by " + instructor + " in " + sampleLectureHallStringArguments[i % numOfLectureHalls] + " (" + "1"
                + " of " + sampleLectureHallIntArguments[i % numOfLectureHalls] + " seats filled)";
            System.out.println("Expected: " + expectedToStringResult);
            System.out.println("Actual:   " + course);
            System.out.println();

            numOfCoursesAssignedToEachInstructor[i % numOfInstructors]++;
            System.out.println("Comparing Instructor's count of courses");
            System.out.println("Expected: " + numOfCoursesAssignedToEachInstructor[i % numOfInstructors]);
            System.out.println("Actual:   " + instructor.getListOfCourses().size());
            System.out.println();
        }
        // Test GPA functionality
        for (Instructor instructor : testUniversity.getInstructors()) {
            for(Course course : instructor.getListOfCourses()) {
                for(Student student : course.getStudents()) {
                    System.out.printf("%s is assigning a random GPA to %s from %s%n", instructor, student, course);
                    double randomGpa = 6 * Math.random() - 1;
                    double priorGpa = student.getGpa();
                    instructor.assignGpa(randomGpa, student);
                    double expectedGpa = (randomGpa > 0 && randomGpa < 4) ? randomGpa : priorGpa;
                    System.out.println("Comparing GPA with assigned GPA");
                    System.out.printf("Expected: %.5f%n", expectedGpa);
                    System.out.printf("Actual:   %.5f%n", student.getGpa());
                    System.out.println();
                }
            }
        }
    } // end method testAcademics

    /**
     * Runs the above tests
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        testOrganization();
        testAcademics();
    } // end method main
} // end class Tester
