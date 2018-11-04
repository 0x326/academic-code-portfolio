import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.util.Scanner;

/**
 * A class for storing a retrievable list of Appointments. Provides a means of storing and loading Appointments
 * to/from files (JSON format).
 * @author John Meyer
 * @since 3/11/2017
 * CSE 271 F
 * Dr. Angel Bravo
 * Project1
 */
public class AppointmentBook {
    // Instance variables
    private ArrayList<Appointment> appointments;

    /**
     * Represents a type of reoccurrence of an Appointment
     */
    public enum appointmentType {
        Onetime, Daily, Monthly
    }

    /**
     * Create an AppointmentBook object
     */
    public AppointmentBook() {
        this.appointments = new ArrayList<Appointment>();
    } // end constructor AppointmentBook

    /**
     * Creates and adds an appointment to this AppointmentBook
     * @param type The type of appointment to add
     *             @see appointmentType
     * @param description The description of the event
     * @param date The date on which the event is to occur
     */
    public void addAppointment(appointmentType type, String description, GregorianCalendar date) {
        // Gather date data (convert month to 1-12 counting system)
        int yearOfDate = date.get(Calendar.YEAR);
        int monthOfDate = date.get(Calendar.MONTH) + 1;
        int dayOfDate = date.get(Calendar.DAY_OF_MONTH);

        // Create appointment
        Appointment appointment = null;
        switch (type) {
            case Onetime:
                appointment = new Onetime(yearOfDate, monthOfDate, dayOfDate, description);
                break;
            case Daily:
                appointment = new Daily(yearOfDate, monthOfDate, dayOfDate, description);
                break;
            case Monthly:
                appointment = new Monthly(yearOfDate, monthOfDate, dayOfDate, description);
                break;
        }
        // Add the appointment
        appointments.add(appointment);
    } // end method addAppointment

    // Project assumption: since the load method is supposed to parse 'each' Appointment in a single file,
    // the project requires the 'save' method to store more than one Appointment in a given file
    /**
     * Saves the given appointment to a JSON-like format
     * @param appointment The Appointment to save
     * @param file The File (object) to which to save the Appointment. Overwrites File if it is corrupt
     * @throws FileNotFoundException File I/O Error
     */
    public static void save(Appointment appointment, File file) throws FileNotFoundException {
        // Record current file content
        String formerFileContents = "";
        Scanner fileIn = null;
        try {
            fileIn = new Scanner(file);
            while (fileIn.hasNext()) {
                formerFileContents += fileIn.nextLine() + "\n";
            }
            formerFileContents = formerFileContents.replaceAll("(\\n|\\r\\n|\\r)?]\\n$", ",");
        }
        catch (FileNotFoundException error) {
            // If there is no file content, proceed
        }
        finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }

        // If file appears to be empty (or corrupt), start a new file
        if (!formerFileContents.startsWith("[")) {
            formerFileContents = "[";
        }

        // Write former content
        PrintWriter fileOut = new PrintWriter(file);
        fileOut.println(formerFileContents);
        // Write object start
        fileOut.print("  {\n");

        // Write object type
        fileOut.print("    \"_objectType\": ");
        if (appointment instanceof Onetime) {
            fileOut.print("\"Onetime\"");
        }
        else if (appointment instanceof Daily) {
            fileOut.print("\"Daily\"");
        }
        else if (appointment instanceof Monthly) {
            fileOut.print("\"Monthly\"");
        }
        fileOut.println(',');

        // Escape special characters in textual description
        String appointmentDescription = appointment.getTextualDescription();
        appointmentDescription = appointmentDescription.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"");
        // Write description
        fileOut.println("    \"textualDescription\": \"" + appointmentDescription + "\",");

        // Write date
        fileOut.println("    \"date\": {");
        fileOut.println("      \"_objectType\": \"GregorianCalendar\",");
        fileOut.println("      \"year\": \"" + appointment.getDate().get(Calendar.YEAR) + "\",");
        fileOut.println("      \"month\": \"" + appointment.getDate().get(Calendar.MONTH) + "\",");
        fileOut.println("      \"day\": \"" + appointment.getDate().get(Calendar.DAY_OF_MONTH) + "\"");
        fileOut.println("    }");

        // Close object and array
        fileOut.println("  }");
        fileOut.println("]");
        fileOut.close();
    } // end method save

    /**
     * Adds the appointment in the given file to the given AppointmentBook
     * @param file The File (object) in which an Appointment is stored
     * @throws FileNotFoundException File I/O Error
     */
    public void load(File file) throws FileNotFoundException {
        // Create ArrayList of Appointments to load
        ArrayList<Appointment> appointmentsToLoad = new ArrayList<Appointment>();
        Scanner fileIn = new Scanner(file);

        // Get file contents
        String fileContents = "";
        while (fileIn.hasNext()) {
            fileContents += fileIn.nextLine().replaceAll("^\\s*(\\S)", "$1").replaceAll("(\\S)\\s*$", "$1");
        }
        fileIn.close();

        // Verify JSON represents an array
        if (fileContents.startsWith("[")) {
            // Divide into elements
            fileContents = fileContents.replaceAll("(^\\[|\\]$)", "");
            String[] fileSegments = fileContents.split("(?<!(\\{|\\[|\\\"|\\w|\\s)),(?=(\\{|\\[|\\\"|\\d))");
            for (String fileSegment : fileSegments) {
                // Verify JSON array element represents an object
                if (fileSegment.startsWith("{")) {
                    // Parse JSON object
                    Object objectRepresentedByJson = parseObject(fileSegment);
                    // Verify JSON object represents an Appointment
                    if (objectRepresentedByJson instanceof Appointment) {
                        Appointment appointmentObject = (Appointment) objectRepresentedByJson;
                        appointmentsToLoad.add(appointmentObject);
                    }
                }
            }
        }

        // Add parsable Appointments
        for (Appointment appointmentObject : appointmentsToLoad) {
            // Extract properties
            appointmentType objectType;
            if (appointmentObject instanceof Onetime) {
                objectType = appointmentType.Onetime;
            }
            else if (appointmentObject instanceof Daily) {
                objectType = appointmentType.Daily;
            }
            else {
                objectType = appointmentType.Monthly;
            }
            String eventDescription = appointmentObject.getTextualDescription();
            GregorianCalendar eventDate = appointmentObject.getDate();
            // Send properties to AppointmentBook
            this.addAppointment(objectType, eventDescription, eventDate);
        }
    } // end method load

    /**
     * Parses an object represented by a JSON fragment
     * @param jsonFragment JSON fragment representing an object
     * @return The Object
     */
    private static Object parseObject(String jsonFragment) {
        // Parse the object and its attributes
        jsonFragment = jsonFragment.replaceAll("(^\\{|\\}$)", "");
        String[] attributes = jsonFragment.replaceAll("(\\\"\\w+\\\"):\\s*(\\{.*?\\}|\\\".*?(?<!\\\\)\\\"|\\d+?\\b)", "$1")
            .replaceAll("(\\\".*?(?<!\\\\)\\\"|\\{.*?\\})(,?)", "$1\n")
            .split("\n");
        String[] attributesContent = jsonFragment.replaceAll("(\\\"\\w+\\\"):\\s*(\\{.*?\\}|\\\".*?(?<!\\\\)\\\"|\\d+?\\b)", "$2")
            .replaceAll("(\\\".*?(?<!\\\\)\\\"|\\{.*?\\})(,?)", "$1\n")
            .split("\n");

        // Clean attributes
        String objectType = "";
        for (int i = 0; i < attributes.length && i < attributesContent.length; i++) {
            // Unescape special characters
            attributes[i] = attributes[i].replaceAll("^\\\"", "").replaceAll("\\\"$", "")
                .replaceAll("\\\\\"", "\"").replaceAll("(\\\\){2}", "$1").replaceAll("\\\\n", "\n");
            attributesContent[i] = attributesContent[i].replaceAll("^\\\"", "").replaceAll("\\\"$", "")
                .replaceAll("\\\\\"", "\"").replaceAll("(\\\\){2}", "$1").replaceAll("\\\\n", "\n");
            // If the object type property is found, store it
            if (attributes[i].equals("_objectType")) {
                objectType = attributesContent[i];
            }
        }

        // Create the object if we know how to make it
        Object returnObject = null;
        if (objectType.equals("GregorianCalendar")) {
            // Gather date info
            int year = -1;
            int month = -1;
            int day = -1;
            try {
                for (int i = 0; i < attributes.length; i++) {
                    if (attributes[i].equals("year")) {
                        year = Integer.parseInt(attributesContent[i]);
                    } else if (attributes[i].equals("month")) {
                        month = Integer.parseInt(attributesContent[i]);
                    } else if (attributes[i].equals("day")) {
                        day = Integer.parseInt(attributesContent[i]);
                    }
                }
                returnObject = new GregorianCalendar(year, month, day);
            }
            catch (NumberFormatException error) {
                // Do nothing
            }
        }
        else if (objectType.equals("Onetime") || objectType.equals("Daily") || objectType.equals("Monthly")) {
            // Gather Appointment info
            String eventDescription = null;
            Object dateObject = null;
            for (int i = 0; i < attributes.length; i++) {
                if (attributes[i].equals("textualDescription")) {
                    eventDescription = attributesContent[i];
                }
                else if (attributes[i].equals("date")) {
                    dateObject = parseObject(attributesContent[i]);
                }
            }
            // Determine whether the JSON has the properties of a valid Appointment
            if (dateObject instanceof GregorianCalendar && eventDescription != null) {
                // Extract date
                GregorianCalendar gregorianDateObject = (GregorianCalendar) dateObject;
                int eventYear = gregorianDateObject.get(Calendar.YEAR);
                int eventMonth = gregorianDateObject.get(Calendar.MONTH) + 1; // Convert to 1-12 counting system
                int eventDay = gregorianDateObject.get(Calendar.DAY_OF_MONTH);
                // Create object
                if (objectType.equals("Onetime")) {
                    returnObject = new Onetime(eventYear, eventMonth, eventDay, eventDescription);
                }
                else if (objectType.equals("Daily")) {
                    returnObject = new Daily(eventYear, eventMonth, eventDay, eventDescription);
                }
                else if (objectType.equals("Monthly")) {
                    returnObject = new Monthly(eventYear, eventMonth, eventDay, eventDescription);
                }
            }
        }
        // if returnObject is null at this point,
        // then this segment of the JSON either: is not a parsable Appointment or is not a parsable GregorianCalendar
        return returnObject;
    } // end method parseObject

    // Project assumption: It is better to have the AppointmentBook class compute the events on a given date internally
    // than to pass the burden to this class' users through a getter
    /**
     * Finds the events which occur on the given date
     * @param date The given date on which the dates are to occur
     * @return An array of the events which occur on the given date
     */
    public Appointment[] getEventsOnDate(GregorianCalendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1; // Convert to 1-12 counting system
        int day = date.get(Calendar.DAY_OF_MONTH);

        // Find reoccurrences
        boolean[] eventDoesOccurOnDate = new boolean[appointments.size()];
        int totalOccurrencesOnDate = 0;
        for (int i = 0; i < appointments.size(); i++) {
            eventDoesOccurOnDate[i] = appointments.get(i).occursOn(year, month, day);
            if (eventDoesOccurOnDate[i]) {
                totalOccurrencesOnDate++;
            }
        }

        // Compile reoccurrences into array
        Appointment[] occurrences = new Appointment[totalOccurrencesOnDate];
        int tallyOfOccurrencesAdded = 0;
        for (int i = 0; i < eventDoesOccurOnDate.length; i++) {
            if (eventDoesOccurOnDate[i]) {
                occurrences[tallyOfOccurrencesAdded++] = appointments.get(i);
            }
        }
        return occurrences;
    } // end method getEventsOnDate

    /**
     * Returns an array of all the events stored in this AppointmentBook
     * @return the array of Appointment's
     */
    public Appointment[] getAllEvents() {
        return appointments.toArray(new Appointment[0]);
    }
} // end class AppointmentBook
