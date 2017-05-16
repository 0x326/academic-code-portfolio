import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A class for a one-time occurrence
 * @author John Meyer
 * @since 3/11/2017
 * CSE 271 F
 * Dr. Angel Bravo
 * Project1
 */
public class Onetime extends Appointment {
    /**
     * Creates a Onetime Appointment
     * @param year The year of the date on which the event is to occur
     * @param month The month of the date on which the event is to occur (on a 1-12 counting system)
     * @param day The day of the date on which the event is to occur
     * @param textualDescription A description of the event
     */
    public Onetime(int year, int month, int day, String textualDescription) {
        super(year, month, day, textualDescription);
    } // end constructor Onetime

    /**
     * Determines whether the given event occurs on the specified date
     * @param year The year of the day to be checked
     * @param month The month of the day to be checked (on a 1-12 counting system)
     * @param day The day to be checked
     * @return True if the event occurs on the given day
     */
    public boolean occursOn(int year, int month, int day) {
        // Convert month to 0-11 counting system
        month--;

        // Pass parameters to Gregorian Calendar to account for lenient dates
        GregorianCalendar dateInQuestion = new GregorianCalendar(year, month, day);
        year = dateInQuestion.get(Calendar.YEAR);
        month = dateInQuestion.get(Calendar.MONTH);
        day = dateInQuestion.get(Calendar.DAY_OF_MONTH);

        int thisYear = this.getDate().get(Calendar.YEAR);
        int thisMonth = this.getDate().get(Calendar.MONTH);
        int thisDay = this.getDate().get(Calendar.DAY_OF_MONTH);

        return (thisYear == year) && (thisMonth == month) && (thisDay == day);
    } // end method occursOn
} // end class Onetime
