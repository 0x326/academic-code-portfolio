/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Employee {
    // Define instance variables
    private String name;
    private double salary;

    /**
     * Returns a String representation of the Employee
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", salary=" + (int) salary + "." + (int) (salary * 10 % 10) + (int) (salary * 100 % 10) +
                '}';
    } // end method toString

    // Define getters and setters
    public String getName() {
        return name;
    } // end method getName

    public void setName(String name) {
        this.name = name;
    } // end method setName

    public double getSalary() {
        return salary;
    } // end method getSalary

    public void setSalary(double salary) {
        if (salary >= 0) {
            this.salary = salary;
        }
    } // end method setSalary

    /**
     * Creates an Employee object
     * @param name His name
     * @param salary HIs salary
     */
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    } // end method Employee
} // end class Employee
