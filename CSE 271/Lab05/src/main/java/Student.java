/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Student extends Person {
    // Defines instance variables
    private String major;

    /**
     * Returns a String presentation of the Student
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Student{" +
                "major='" + major + '\'' +
                "} " + super.toString();
    } // end method toString

    // Define getters and setters
    public String getMajor() {
        return major;
    } // end method getMajor

    public void setMajor(String major) {
        this.major = major;
    } // end method setMajor

    public Student(String name, String dateOfBirth, String major) {
        super(name, dateOfBirth);
        this.major = major;
    } // end method Student
} // end class Student
