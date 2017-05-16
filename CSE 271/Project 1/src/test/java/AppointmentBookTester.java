import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * A demo class which utilizes an AppointmentBook to display all the sample events on a given date
 * @author John Meyer
 * @since 3/11/2017
 * CSE 271 F
 * Dr. Angel Bravo
 * Project1
 */
public class AppointmentBookTester {
    /**
     * Fills an AppointmentBook with sample data and displays the Appointments on a given date specified by the user
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        String programUIHeader = "==== APPOINTMENT BOOK TESTER ====";
        AppointmentBook sampleBook = new AppointmentBook();

        // Create parallel arrays for sample events
        int[] sampleDates = {
            2017,  1, 23,
            2017,  1, 25,
            2017,  1, 31,
            2017,  2,  1,
            2017,  2,  6,
            2017,  2,  1,
            2017,  3,  4,
            2017,  2, 13,
            2017,  2,  7
        };
        String[] sampleDescriptions = {
            "Academic Work",
            "Homework",
            "Monthly Quiz",
            "Spring ICE",
            "Winter Mega Fair",
            "Start-of-the-month kickoff",
            "Org meeting",
            "Music rehearsal",
            "Group Project"
        };
        AppointmentBook.appointmentType[] sampleDateTypes = {
            AppointmentBook.appointmentType.Daily,
            AppointmentBook.appointmentType.Daily,
            AppointmentBook.appointmentType.Monthly,
            AppointmentBook.appointmentType.Onetime,
            AppointmentBook.appointmentType.Onetime,
            AppointmentBook.appointmentType.Monthly,
            AppointmentBook.appointmentType.Monthly,
            AppointmentBook.appointmentType.Daily,
            AppointmentBook.appointmentType.Monthly
        };
        if (sampleDates.length != sampleDescriptions.length * 3 || sampleDates.length != sampleDateTypes.length * 3) {
            System.out.println("There is an inconsistent number of sample dates. Cannot build AppointmentBook");
            return;
        }

        // Add events to AppointmentBook
        GregorianCalendar eventDate;
        for (int i = 2; i < sampleDates.length; i+= 3) {
            // Convert month to 0-11 counting system
            eventDate = new GregorianCalendar(sampleDates[i - 2], sampleDates[i - 1] - 1, sampleDates[i]);
            sampleBook.addAppointment(sampleDateTypes[i / 3], sampleDescriptions[i / 3], eventDate);
        }

        // Build text UI
        Scanner keyboard = new Scanner(System.in);
        System.out.println(programUIHeader);
        System.out.println();
        System.out.println("(Enter nothing to quit)");
        System.out.println();
        System.out.println("To list the events of a given day,");
        String userInput;
        do {
            System.out.print("Enter a date within the vicinity of February and March of 2017 (YYYY/MM/DD): ");
            userInput = keyboard.nextLine();
            // Parse user input
            String[] dateComponents = userInput.split("[-/ .\\\\]");
            if (dateComponents.length >= 3) {
                try {
                    int year = Integer.parseInt(dateComponents[0]);
                    int month = Integer.parseInt(dateComponents[1]);
                    int day = Integer.parseInt(dateComponents[2]);
                    // Convert month to 0-11 counting system
                    GregorianCalendar date = new GregorianCalendar(year, month - 1, day);
                    System.out.print("Showing events on " + year + "/" + month + "/" + day);
                    if (date.isLenient()) {
                        System.out.print(" (using a lenient interpreter)");
                    }
                    System.out.println();

                    Appointment[] appointments = sampleBook.getEventsOnDate(date);
                    for (Appointment appointment : appointments) {
                        System.out.println(" " + appointment.getTextualDescription());
                    }
                }
                catch (NumberFormatException error) {
                    System.out.println("Date is malformed. Try again");
                }
            }
            else {
                System.out.println("Year, Month, and Day could not be parsed. ");
            }
            System.out.println();
        } while (!userInput.equalsIgnoreCase(""));
        System.out.println("Ending program");
        keyboard.close();
    } // end method main
} // enc class AppointmentBookTester
