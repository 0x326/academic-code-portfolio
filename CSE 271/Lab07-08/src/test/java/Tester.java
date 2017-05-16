// Created by John Meyer on 3/21/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

import edu.miamioh.meyerjm.*;
import org.junit.Test;

import java.util.ArrayList;

public class Tester {
    // Define testing variables
    private String[] sampleInstructorStringArguments = {
        "bravosad", "bravosad@miamioh.edu",
        "Bo.Brinkman", "Bo.Brinkman@miamioh.edu",
        "krumpenj", "krumpenj@MiamiOH.edu"
    };
    private int[] sampleInstructorIntArguments = {
        5000000,
        5000001,
        4999999
    };
    private String[] sampleStudentStringArguments = {
        "meyerjm", "meyerjm@miamioh.edu", "Computer Engineering",
        "doejohn", "doejohn@miamioh.edu", "Undecided",
        "doejane", "doejane@miamioh.edu", "General Engineering"
    };
    private String[] sampleAdminStaffStringArguments = {
        "kirkmebp", "kirkmebp@miamioh.edu"
    };
    private int[] sampleAdminStaffIntArguments = {
        7000000
    };
    private String[] sampleCourseStringArguments = {
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
    private int[] sampleLectureHallIntArguments = {
        100,
        20,
        200
    };
    private String[] sampleLectureHallStringArguments = {
        "BEN 102",
        "BAC 255",
        "KRG 311"
    };
    private ArrayList<Instructor> testInstructors = null;
    private ArrayList<AdminStaff> testAdmins = null;
    private ArrayList<Student> testStudents = null;
    private ArrayList<Course> testCourses = null;
    private ArrayList<LectureHall> testLectureHalls = null;

    private void fillUniversity(University testUniversity) {
        // Create test objects
        assert sampleInstructorIntArguments.length * 2 == sampleInstructorStringArguments.length;
        testInstructors = new ArrayList<Instructor>();
        for (int i = 1; i < sampleInstructorStringArguments.length; i += 2) {
            testInstructors.add(new Instructor(sampleInstructorStringArguments[i - 1], sampleInstructorStringArguments[i], sampleInstructorIntArguments[i / 2]));
        }
        assert sampleAdminStaffIntArguments.length * 2 == sampleAdminStaffStringArguments.length;
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
        assert sampleLectureHallIntArguments.length == sampleLectureHallStringArguments.length;
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
    }

    /**
     * Tests adding to a University and retrieval from it
     */
    @Test
    public void testOrganization() {
        University testUniversity = new University();
        fillUniversity(testUniversity);
        System.out.println("Comparing Instructors");
        System.out.println("Expected: " + testInstructors);
        System.out.println("Actual:   " + testUniversity.getInstructors());
        System.out.println();
        assert testInstructors.equals(testUniversity.getInstructors());

        System.out.println("Comparing AdminStaff");
        System.out.println("Expected: " + testAdmins);
        System.out.println("Actual:   " + testUniversity.getAdmins());
        System.out.println();
        assert testAdmins.equals(testUniversity.getAdmins());

        System.out.println("Comparing Students");
        System.out.println("Expected: " + testStudents);
        System.out.println("Actual:   " + testUniversity.getStudents());
        System.out.println();
        assert testStudents.equals(testUniversity.getStudents());

        System.out.println("Comparing Courses");
        System.out.println("Expected: " + testCourses);
        System.out.println("Actual:   " + testUniversity.getCourses());
        System.out.println();
        assert testCourses.equals(testUniversity.getCourses());

        System.out.println("Comparing LectureHalls");
        System.out.println("Expected: " + testLectureHalls);
        System.out.println("Actual:   " + testUniversity.getLectureHalls());
        System.out.println();
        assert testLectureHalls.equals(testUniversity.getLectureHalls());

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
        assert allTestPeople.equals(recordedPeople);
    }

    /**
     * Tests the capabilities of AdminStaff and Instructor objects
     */
    @Test
    public void testAcademics() {
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
            assert course.toString().equals(expectedToStringResult);

            numOfCoursesAssignedToEachInstructor[i % numOfInstructors]++;
            System.out.println("Comparing Instructor's count of courses");
            System.out.println("Expected: " + numOfCoursesAssignedToEachInstructor[i % numOfInstructors]);
            System.out.println("Actual:   " + instructor.getListOfCourses().size());
            System.out.println();
            assert instructor.getListOfCourses().size() == numOfCoursesAssignedToEachInstructor[i % numOfInstructors];
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
                    assert Math.abs(student.getGpa() - expectedGpa) < 1e-5;
                }
            }
        }
    }
}
