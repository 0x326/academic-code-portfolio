/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Instructor extends Person {
    // Define instance variables
    private double salary;

    /**
     * Returns a String presentation of the Instructor
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Instructor{" +
                "salary=" + (int) salary + "." + (int) (salary * 10 % 10) + (int) (salary * 100 % 10) +
                "} " + super.toString();
    } // end method toString

    // Define getters and setters
    public double getSalary() {
        return salary;
    } // end method getSalary

    public void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary;
        }
    } // end method setSalary

    /**
     * Creates an Instructor object
     * @param name The name of the instructor
     * @param dateOfBirth His date of birth
     * @param salary His salary
     */
    public Instructor(String name, String dateOfBirth, double salary) {
        super(name, dateOfBirth);
        this.salary = salary;
    } // end method Instructor
} // end class Instructor
