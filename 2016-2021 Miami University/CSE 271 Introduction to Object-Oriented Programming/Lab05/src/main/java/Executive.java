/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Executive extends Manager {
    // Define instances variables
    private String officeLocation;

    /**
     * Returns a String representation of the Executive
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Executive{" +
                "officeLocation='" + officeLocation + '\'' +
                "} " + super.toString();
    } // end method toString

    // Define getters and setters
    public String getOfficeLocation() {
        return officeLocation;
    } // end method getOfficeLocation

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    } // end method setOfficeLocation

    /**
     * Creates an Executive object
     * @param name His name
     * @param salary His salary
     * @param department His department
     * @param officeLocation His office location
     */
    public Executive(String name, double salary, String department, String officeLocation) {
        super(name, salary, department);
        this.officeLocation = officeLocation;
    } // end method Executive
} // end class Executive
