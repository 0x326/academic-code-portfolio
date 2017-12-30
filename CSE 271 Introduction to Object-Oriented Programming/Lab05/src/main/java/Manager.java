/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Manager extends Employee {
    // Define instance variables
    private String department;

    /**
     * Returns a String representation of the Manager
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Manager{" +
                "department='" + department + '\'' +
                "} " + super.toString();
    } // end method toString

    // Define getters and setters
    public String getDepartment() {
        return department;
    } // end method getDepartment

    public void setDepartment(String department) {
        this.department = department;
    } // end method setDepartment

    /**
     * Creates a Manager object
     * @param name His name
     * @param salary His salary
     * @param department His department
     */
    public Manager(String name, double salary, String department) {
        super(name, salary);
        this.department = department;
    } // end method Manager
} // end class Manager
