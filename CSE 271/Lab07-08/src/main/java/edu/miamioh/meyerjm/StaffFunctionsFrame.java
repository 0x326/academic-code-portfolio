// Created by John Meyer on 3/25/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08


package edu.miamioh.meyerjm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Creates a window to do AdminStaff related tasks
 */
public class StaffFunctionsFrame extends JFrame {
    private University university;
    private int selectedAdminIndex;
    private JLabel selectedAdmin;
    private int selectedStudentIndex;
    private JLabel selectedStudent;
    private int selectedCourseIndex;
    private JLabel selectedCourse;
    private int selectedLectureHallIndex;
    private JLabel selectedLectureHall;
    private int selectedInstructorIndex;
    private JLabel selectedInstructor;
    
    private JTextArea universityAdminList;
    private JTextArea universityStudentList;
    private JTextArea universityCourseList;
    private JTextArea universityLectureHallList;
    private JTextArea universityInstructorList;

    private JTextField studentGpaEntryTextField;
    private JTextArea instructorCoursesList;

    /**
     * Creates an StaffFunctionsFrame object
     * @param university The university object on which to operate and display data
     */
    public StaffFunctionsFrame(University university) {
        this.university = university;
        this.selectedAdminIndex = -1;
        this.selectedStudentIndex = -1;
        this.selectedCourseIndex = -1;
        this.selectedLectureHallIndex = -1;
        this.selectedInstructorIndex = -1;

        // Build window
        setTitle("Admin Function");
        setSize(550, 800);
        JPanel functionPanel = new JPanel();
        add(functionPanel);
        // Add university data
        this.universityAdminList = new JTextArea();
        this.universityStudentList = new JTextArea();
        this.universityCourseList = new JTextArea();
        this.universityLectureHallList = new JTextArea();
        this.universityInstructorList = new JTextArea();

        this.universityAdminList.setEditable(false);
        this.universityStudentList.setEditable(false);
        this.universityCourseList.setEditable(false);
        this.universityLectureHallList.setEditable(false);
        this.universityInstructorList.setEditable(false);

        functionPanel.add(this.universityAdminList);
        functionPanel.add(this.universityStudentList);
        functionPanel.add(this.universityCourseList);
        functionPanel.add(this.universityLectureHallList);
        functionPanel.add(this.universityInstructorList);
        fillUniversityInfoTextAreas();

        // Create selector buttons and labels
        JButton previousAdminButton = new JButton("Previous Admin");
        JButton nextAdminButton = new JButton("Next Admin");
        this.selectedAdmin = new JLabel("No Admin Selected");
        previousAdminButton.addActionListener(new DecrementAdminIndex());
        nextAdminButton.addActionListener(new IncrementAdminIndex());

        JButton previousStudentButton = new JButton("Previous Student");
        JButton nextStudentButton = new JButton("Next Student");
        this.selectedStudent = new JLabel("No Student Selected");
        previousStudentButton.addActionListener(new DecrementStudentIndex());
        nextStudentButton.addActionListener(new IncrementStudentIndex());

        JButton previousCourseButton = new JButton("Previous Course");
        JButton nextCourseButton = new JButton("Next Course");
        this.selectedCourse = new JLabel("No Course Selected");
        previousCourseButton.addActionListener(new DecrementCourseIndex());
        nextCourseButton.addActionListener(new IncrementCourseIndex());

        JButton previousLectureHallButton = new JButton("Previous Lecture Hall");
        JButton nextLectureHallButton = new JButton("Next Lecture Hall");
        this.selectedLectureHall = new JLabel("No LectureHall Selected");
        previousLectureHallButton.addActionListener(new DecrementLectureHallIndex());
        nextLectureHallButton.addActionListener(new IncrementLectureHallIndex());

        JButton previousInstructorButton = new JButton("Previous Instructor");
        JButton nextInstructorButton = new JButton("Next Instructor");
        this.selectedInstructor = new JLabel("No Instructor Selected");
        previousInstructorButton.addActionListener(new DecrementInstructorIndex());
        nextInstructorButton.addActionListener(new IncrementInstructorIndex());

        // Attach selector buttons and labels
        functionPanel.add(previousAdminButton);
        functionPanel.add(nextAdminButton);
        functionPanel.add(this.selectedAdmin);
        functionPanel.add(previousStudentButton);
        functionPanel.add(nextStudentButton);
        functionPanel.add(this.selectedStudent);
        functionPanel.add(previousCourseButton);
        functionPanel.add(nextCourseButton);
        functionPanel.add(this.selectedCourse);
        functionPanel.add(previousLectureHallButton);
        functionPanel.add(nextLectureHallButton);
        functionPanel.add(this.selectedLectureHall);
        functionPanel.add(previousInstructorButton);
        functionPanel.add(nextInstructorButton);
        functionPanel.add(this.selectedInstructor);

        // Create operator buttons
        JButton enrollStudentInCourse = new JButton("Enroll the selected student in the selected course using the selected admin");
        JButton assignCourseALectureHall = new JButton("Assign selected lecture hall to the selected course using the selected admin");
        JButton assignCourseAnInstructor = new JButton("Assign selected instructor to the selected course using the selected admin");
        JButton assignStudentGpa = new JButton("Give the selected student the entered GPA using the selected instructor");
        JButton getInstructorsCourses = new JButton("Display the courses which the selected instructor is teaching");
        enrollStudentInCourse.addActionListener(new EnrollStudentInCourse());
        assignCourseALectureHall.addActionListener(new AssignCourseALectureHall());
        assignCourseAnInstructor.addActionListener(new AssignCourseAnInstructor());
        assignStudentGpa.addActionListener(new AssignStudentGpa());
        getInstructorsCourses.addActionListener(new GetInstructorsCourses());

        this.studentGpaEntryTextField = new JTextField(20);
        this.studentGpaEntryTextField.setText("Enter a GPA here");
        this.instructorCoursesList = new JTextArea();
        this.instructorCoursesList.setEditable(false);

        // Attach operator buttons
        functionPanel.add(enrollStudentInCourse);
        functionPanel.add(assignCourseALectureHall);
        functionPanel.add(assignCourseAnInstructor);
        functionPanel.add(assignStudentGpa);
        functionPanel.add(this.studentGpaEntryTextField);
        functionPanel.add(getInstructorsCourses);
        functionPanel.add(this.instructorCoursesList);
    } // end constructor StaffFunctionsFrame

    /**
     * Updates the university JTextAreas with the latest info
     */
    private void fillUniversityInfoTextAreas() {
        this.universityAdminList.setText(createObjectList("All University Admins", university.getAdmins()));
        this.universityStudentList.setText(createObjectList("All University Students", university.getStudents()));
        this.universityCourseList.setText(createObjectList("All University Courses", university.getCourses()));
        this.universityLectureHallList.setText(createObjectList("All University Lecture Halls", university.getLectureHalls()));
        this.universityInstructorList.setText(createObjectList("All University Instructors", university.getInstructors()));
    }

    /**
     * Given an ArrayList of Objects, creates a String of each element separated by newline characters.
     * This String is prefixed with a title.
     * @param titleText The title of this list
     * @param arrayList The list to be parsed into a string
     * @return A String representation of all the elements
     */
    private String createObjectList(String titleText, ArrayList<?> arrayList) {
        String listText = "";
        listText += ("=== " + titleText + " ===\n");
        for (Object object : arrayList) {
            listText += ("" + object + "\n");
        }
        return listText;
    } // end method createObjectList
    
    /**
     * Updates the Admin selector JLabel text
     */
    private void updateAdminLabelText() {
        selectedAdmin.setText("" + university.getAdmins().get(selectedAdminIndex));
    }

    /**
     * Updates the Student selector JLabel text
     */
    private void updateStudentLabelText() {
        selectedStudent.setText("" + university.getStudents().get(selectedStudentIndex));
    }

    /**
     * Updates the Course selector JLabel text
     */
    private void updateCourseLabelText() {
        selectedCourse.setText("" + university.getCourses().get(selectedCourseIndex));
    }

    /**
     * Updates the LectureHall JLabel text
     */
    private void updateLectureHallLabelText() {
        selectedLectureHall.setText("" + university.getLectureHalls().get(selectedLectureHallIndex)
            + "(" + university.getLectureHalls().get(selectedLectureHallIndex).getCapacity() + "seats)"
        );
    }

    /**
     * Updates the Instructor JLabel text
     */
    private void updateInstructorLabelText() {
        selectedInstructor.setText("" + university.getInstructors().get(selectedInstructorIndex));
    }

    /**
     * An action listener which increments the index of the selected Admin
     */
    class IncrementAdminIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedAdminIndex++;
            if (selectedAdminIndex == university.getAdmins().size()) {
                selectedAdminIndex = 0;
            }
            updateAdminLabelText();
        }
    } // end class IncrementAdminIndex

    /**
     * An action listener which decrements the index of the selected AdminStaff
     */
    class DecrementAdminIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedAdminIndex <= 0) {
                selectedAdminIndex = university.getAdmins().size();
            }
            selectedAdminIndex--;
            updateAdminLabelText();
        }
    } // end class decrementAdminIndex

    /**
     * An action listener which increments the index of the selected Student
     */
    class IncrementStudentIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedStudentIndex++;
            if (selectedStudentIndex == university.getStudents().size()) {
                selectedStudentIndex = 0;
            }
            updateStudentLabelText();
        }
    } // end class incrementStudentIndex

    /**
     * An action listener which decrements the index of the selected Student
     */
    class DecrementStudentIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedStudentIndex <= 0) {
                selectedStudentIndex = university.getStudents().size();
            }
            selectedStudentIndex--;
            updateStudentLabelText();
        }
    } // end class decrementStudentIndex

    /**
     * An action listener which increments the index of the selected Course
     */
    class IncrementCourseIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedCourseIndex++;
            if (selectedCourseIndex == university.getCourses().size()) {
                selectedCourseIndex = 0;
            }
            updateCourseLabelText();
        }
    } // end class incrementCourseIndex

    /**
     * An action listener which decrements the index of the selected Course
     */
    class DecrementCourseIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedCourseIndex <= 0) {
                selectedCourseIndex = university.getCourses().size();
            }
            selectedCourseIndex--;
            updateCourseLabelText();
        }
    } // end class decrementCourseIndex

    /**
     * An action listener which increments the index of the selected LectureHall
     */
    class IncrementLectureHallIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedLectureHallIndex++;
            if (selectedLectureHallIndex == university.getLectureHalls().size()) {
                selectedLectureHallIndex = 0;
            }
            updateLectureHallLabelText();
        }
    } // end class incrementLectureHallIndex

    /**
     * An action listener which decrements the index of the selected LectureHall
     */
    class DecrementLectureHallIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedLectureHallIndex <= 0) {
                selectedLectureHallIndex = university.getLectureHalls().size();
            }
            selectedLectureHallIndex--;
            updateLectureHallLabelText();
        }
    } // end class decrementLectureHallIndex

    /**
     * An action listener which increments the index of the selected Instructor
     */
    class IncrementInstructorIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            selectedInstructorIndex++;
            if (selectedInstructorIndex == university.getInstructors().size()) {
                selectedInstructorIndex = 0;
            }
            updateInstructorLabelText();
        }
    } // end class incrementInstructorIndex

    /**
     * An action listener which decrements the index of the selected Instructor
     */
    class DecrementInstructorIndex implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedInstructorIndex <= 0) {
                selectedInstructorIndex = university.getInstructors().size();
            }
            selectedInstructorIndex--;
            updateInstructorLabelText();
        }
    } // end class decrementInstructorIndex

    /**
     * Uses the selected AdminStaff to enroll the selected Student in the selected Course
     */
    class EnrollStudentInCourse implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedAdminIndex != -1 && selectedStudentIndex != -1 && selectedCourseIndex != -1) {
                AdminStaff admin = university.getAdmins().get(selectedAdminIndex);
                Student student = university.getStudents().get(selectedStudentIndex);
                Course course = university.getCourses().get(selectedCourseIndex);
                admin.enroll(student, course);
                updateCourseLabelText();
                fillUniversityInfoTextAreas();
            }
        }
    } // end method EnrollStudentInCourse

    /**
     * Uses the selected AdminStaff to assign the selected LectureHall to the selected Course
     */
    class AssignCourseALectureHall implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedAdminIndex != -1 && selectedCourseIndex != -1 && selectedLectureHallIndex != -1) {
                AdminStaff admin = university.getAdmins().get(selectedAdminIndex);
                Course course = university.getCourses().get(selectedCourseIndex);
                LectureHall lectureHall = university.getLectureHalls().get(selectedLectureHallIndex);
                admin.assign(course, lectureHall);
                updateCourseLabelText();
                fillUniversityInfoTextAreas();
            }
        }
    } // end method AssignCourseALectureHall

    /**
     * Uses the selected AdminStaff to assign the selected Instructor to the selected Course
     */
    class AssignCourseAnInstructor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedAdminIndex != -1 && selectedCourseIndex != -1 && selectedInstructorIndex != -1) {
                AdminStaff admin = university.getAdmins().get(selectedAdminIndex);
                Course course = university.getCourses().get(selectedCourseIndex);
                Instructor instructor = university.getInstructors().get(selectedInstructorIndex);
                admin.assign(course, instructor);
                updateCourseLabelText();
                fillUniversityInfoTextAreas();
            }
        }
    } // end method AssignCourseAnInstructor

    /**
     * Uses the selected Instructor to assign the entered GPA to the selected Student
     */
    class AssignStudentGpa implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (selectedInstructorIndex != -1 && selectedStudentIndex != -1) {
                    Instructor instructor = university.getInstructors().get(selectedInstructorIndex);
                    Student student = university.getStudents().get(selectedStudentIndex);
                    double gpa = Double.parseDouble(studentGpaEntryTextField.getText());
                    instructor.assignGpa(gpa, student);
                    updateStudentLabelText();
                    fillUniversityInfoTextAreas();
                }
            }
            catch (NumberFormatException error) {
                // Do nothing
            }
        }
    } // end method AssignStudentGpa

    /**
     * Displays the courses assigned to the selected Instructor
     */
    class GetInstructorsCourses implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedInstructorIndex != -1) {
                Instructor instructor = university.getInstructors().get(selectedInstructorIndex);
                instructorCoursesList.setText(createObjectList("" + instructor, instructor.getListOfCourses()));
            }
        }
    } // end method GetInstructorsCourses
} // end class StaffFunctionsFrame
