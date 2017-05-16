import java.util.GregorianCalendar;

/**
 * A class for an event
 * @author John Meyer
 * @since 3/11/2017
 * CSE 271 F
 * Dr. Angel Bravo
 * Project1
 */
public abstract class Appointment {
    // Instance variables
    private GregorianCalendar date;
    private String textualDescription;

    /**
     * Creates an Appointment object
     * @param year The year of the date on which the event is to occur
     * @param month The month of the date on which the event is to occur (on a 1-12 counting system)
     * @param day The day of the date on which the event is to occur
     * @param textualDescription A description of the event
     */
    public Appointment(int year, int month, int day, String textualDescription) {
        this.date = new GregorianCalendar();
        // Convert month to 0-11 counting system
        month--;
        this.date.set(year, month , day);
        this.textualDescription = textualDescription;
    } // end constructor Appointment

    /**
     * Determines whether the given event occurs on the specified date
     * @param year The year of the day to be checked
     * @param month The month of the day to be checked (on a 1-12 counting system)
     * @param day The day to be checked
     * @return True if the event occurs on the given day
     */
    public abstract boolean occursOn(int year, int month, int day);

    /**
     * Gets the date
     * @return The date
     */
    public GregorianCalendar getDate() {
        return date;
    } // enc method getDate

    /**
     * Gets the description of the event
     * @return The description
     */
    public String getTextualDescription() {
        return textualDescription;
    } // end method getTextualDescription
} // end class Appointment
