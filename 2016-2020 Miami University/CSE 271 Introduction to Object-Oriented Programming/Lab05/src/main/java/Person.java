/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Person {
    // Define instance variables
    private String name;
    private String dateOfBirth;

    /**
     * Converts to a String presentation
     * @return The String presentation
     */
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                '}';
    } // end method toString

    /**
     * Creates a Person object
     * @param name The person's name
     * @param dateOfBirth The person's date of birth
     */
    public Person(String name, String dateOfBirth) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    } // end method Person

    // Define getters and setter
    public String getName() {
        return name;
    } // end method getName

    public void setName(String name) {
        this.name = name;
    } // end method setName

    public String getDateOfBirth() {
        return dateOfBirth;
    } // end method getDateOfBirth

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    } // end method setDateOfBirth
} // end class Person
