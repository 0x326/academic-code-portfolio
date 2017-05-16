import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Tester {

    @Test
    public void testAppointmentConstructor() {
        int givenYear = 2016;
        int givenMonth = 3;
        int givenDay = 3;
        Onetime appointment = new Onetime(givenYear, givenMonth, givenDay, "Casual Meeting");
        int itsYear = appointment.getDate().get(Calendar.YEAR);
        int itsMonth = appointment.getDate().get(Calendar.MONTH) + 1; // Convert to 1-12 counting system
        int itsDay = appointment.getDate().get(Calendar.DAY_OF_MONTH);
        assert (givenYear == itsYear) && (givenMonth == itsMonth) && (givenDay == itsDay);
    }

    @Test
    public void testOneTimeOccursOn() {
        int[] testDates = {
            2017,  3,  3,
            2017,  5,  1,
            2017, 12, 15,
            2018, 13,  1,
            2017,  2, 28,
            2017,  2, 29,
            2018,  2, 29,
            1515,  1,  5,
            1920,  2, 29
        };
        assert testDates.length % 3 == 0;
        Onetime appointment;
        for (int i = 2; i < testDates.length; i += 3) {
            appointment = new Onetime(testDates[i - 2], testDates[i - 1], testDates[i], "Important Date");
            for (int j = 2; j < testDates.length; j += 3) {
                assert appointment.occursOn(testDates[j - 2], testDates[j - 1], testDates[j]) == (i == j) :
                    "Test fails on " + testDates[j - 2] + "/" + testDates[j - 1] + "/" + testDates[j];
            }
        }
    }

    @Test
    public void testDailyOccursOn() {
        int[] realTestDates = {
            1920, 12, 15,
            1921,  5, 31,
            1921,  6,  1,
            2000,  1,  1,
            2000,  1, 30,
            2000,  2,  1,
            2000,  2, 29,
            2000,  3, 31,
            2000,  4, 30
        };
        int[] fakeTestDates = {
            2001,  1, 32,
            2001,  2, 29,
            2001,  4, 31
        };
        assert realTestDates.length % 3 == 0;
        assert fakeTestDates.length % 3 == 0;
        Daily appointment;
        for (int i = 2; i < realTestDates.length; i += 3) {
            appointment = new Daily(realTestDates[i - 2], realTestDates[i - 1], realTestDates[i], "Important Date");
            for (int j = 2; j < realTestDates.length; j += 3) {
                assert appointment.occursOn(realTestDates[j - 2], realTestDates[j - 1], realTestDates[j]) == (i >= j) :
                    "" + realTestDates[i - 2] + "/" + realTestDates[i - 1] + "/" + realTestDates[i] +
                    " ought to occur on " + realTestDates[j - 2] + "/" + realTestDates[j - 1] + "/" + realTestDates[j];
            }
            for (int j = 2; j < fakeTestDates.length; j += 3) {
                assert !appointment.occursOn(fakeTestDates[j - 2], fakeTestDates[j - 1], fakeTestDates[j]) :
                    "" + realTestDates[i - 2] + "/" + realTestDates[i - 1] + "/" + realTestDates[i] +
                    " ought not to occur on " + fakeTestDates[j - 2] + "/" + fakeTestDates[j - 1] + "/" + fakeTestDates[j];
            }
        }
    }

    @Test
    public void testMonthlyOccursOn() {
        int[][] validReoccurrences = {
            {
                2017,  1,  1,
                2017,  2,  1,
                2017,  5,  1,
                2018,  1,  1,
            },
            {
                2017,  1, 31,
                2017,  2, 28,
                2017,  3, 31,
                2017,  4, 30,
                2017,  5, 31,
                2019,  2, 28,
                2020,  2, 29
            },
            {
                2016,  2, 29,
                2016,  3, 29,
                2016,  4, 29,
                2017,  2, 28,
                2018,  2, 28,
                2019,  2, 28,
                2020,  2, 29
            }
        };
        for (int[] testCases : validReoccurrences) {
            assert testCases.length % 3 == 0 && testCases.length >= 3;
        }
        Monthly appointment;
        for (int[] validReoccurrence : validReoccurrences) {
            appointment = new Monthly(validReoccurrence[0], validReoccurrence[1], validReoccurrence[2], "Important Date");
            for (int j = 5; j < validReoccurrence.length; j += 3) {
                assert appointment.occursOn(validReoccurrence[j - 2], validReoccurrence[j - 1], validReoccurrence[j]) :
                    "" + validReoccurrence[0] + "/" + validReoccurrence[1] + "/" + validReoccurrence[2] + " ought to occur on " +
                    validReoccurrence[j - 2] + "/" + validReoccurrence[j - 1] + "/" + validReoccurrence[j];
            }
        }
    }

    @Test
    public void testAppointmentBookSaveLoad() throws FileNotFoundException {
        AppointmentBook testBook = new AppointmentBook();
        fillAppointmentBook(testBook);

        // Compute unique filename based on the current time
        Date presentDate = new Date();
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(presentDate);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        String filename = "testFiles\\" + year + "-" + month + "-" + day;

        // Erase previous test file
        File file = new File(filename + ".json");
        PrintWriter fileOut = new PrintWriter(file);
        fileOut.close();
        // Save files
        Appointment[] appointments = testBook.getAllEvents();
        for (Appointment appointment : appointments) {
            AppointmentBook.save(appointment, file);
        }

        // Read files
        AppointmentBook newBook = new AppointmentBook();
        newBook.load(file);

        // Determine whether the AppointmentBooks are identical
        Appointment[] newAppointments = newBook.getAllEvents();
        assert newAppointments.length == appointments.length;
        for (int i = 0; i < appointments.length; i++) {
            // Find type
            AppointmentBook.appointmentType oldAppointmentType = null;
            if (appointments[i] instanceof Onetime) {
                oldAppointmentType = AppointmentBook.appointmentType.Onetime;
            }
            else if (appointments[i] instanceof Daily) {
                oldAppointmentType = AppointmentBook.appointmentType.Daily;
            }
            else if (appointments[i] instanceof Monthly) {
                oldAppointmentType = AppointmentBook.appointmentType.Monthly;
            }
            assert oldAppointmentType != null;
            AppointmentBook.appointmentType newAppointmentType = null;
            if (newAppointments[i] instanceof Onetime) {
                newAppointmentType = AppointmentBook.appointmentType.Onetime;
            }
            else if (newAppointments[i] instanceof Daily) {
                newAppointmentType = AppointmentBook.appointmentType.Daily;
            }
            else if (newAppointments[i] instanceof Monthly) {
                newAppointmentType = AppointmentBook.appointmentType.Monthly;
            }
            assert newAppointmentType != null;
            assert oldAppointmentType == newAppointmentType;
            assert appointments[i].getTextualDescription().equals(newAppointments[i].getTextualDescription());
            assert appointments[i].getDate().get(Calendar.YEAR) == newAppointments[i].getDate().get(Calendar.YEAR);
            assert appointments[i].getDate().get(Calendar.MONTH) == newAppointments[i].getDate().get(Calendar.MONTH);
            assert appointments[i].getDate().get(Calendar.DAY_OF_MONTH) == newAppointments[i].getDate().get(Calendar.DAY_OF_MONTH);
        }
    }

    /**
     * Fills an Appointment Book with test data
     * @param testBook the AppointmentBook to fill
     */
    private void fillAppointmentBook(AppointmentBook testBook) {
        // Create parallel arrays for sample events
        int[] sampleDates = {
            2017,  1, 23,
            2017,  1, 25,
            2017,  1, 30,
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
        assert (sampleDates.length == (sampleDescriptions.length * 3)) && (sampleDates.length == (sampleDateTypes.length * 3)) :
            "There is an inconsistent number of test dates";

        // Add events to AppointmentBook
        GregorianCalendar eventDate;
        for (int i = 2; i < sampleDates.length; i+= 3) {
            eventDate = new GregorianCalendar(sampleDates[i-2], sampleDates[i-1] - 1, sampleDates[i]);
            testBook.addAppointment(sampleDateTypes[i / 3], sampleDescriptions[i / 3], eventDate);
        }
    }

    @Test
    public void testAppointmentBookSaveLoadWithBadData() throws FileNotFoundException {
        AppointmentBook testBook = new AppointmentBook();
        fillAppointmentBookWithBadData(testBook);

        // Compute unique filename based on the current time
        Date presentDate = new Date();
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(presentDate);
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        int day = now.get(Calendar.DAY_OF_MONTH);
        String filename = "testFiles\\" + year + "-" + month + "-" + day;

        // Erase previous test file
        File file = new File(filename + ".json");
        PrintWriter fileOut = new PrintWriter(file);
        fileOut.close();
        // Save files
        Appointment[] appointments = testBook.getAllEvents();
        for (int i = 0; i < appointments.length; i++) {
            AppointmentBook.save(appointments[i], file);
        }

        // Read files
        AppointmentBook newBook = new AppointmentBook();
        newBook.load(file);

        // Determine whether the AppointmentBooks are identical
        Appointment[] newAppointments = newBook.getAllEvents();
        assert newAppointments.length == appointments.length;
        for (int i = 0; i < appointments.length; i++) {
            // Find type
            AppointmentBook.appointmentType oldAppointmentType = null;
            if (appointments[i] instanceof Onetime) {
                oldAppointmentType = AppointmentBook.appointmentType.Onetime;
            }
            else if (appointments[i] instanceof Daily) {
                oldAppointmentType = AppointmentBook.appointmentType.Daily;
            }
            else if (appointments[i] instanceof Monthly) {
                oldAppointmentType = AppointmentBook.appointmentType.Monthly;
            }
            assert oldAppointmentType != null;
            AppointmentBook.appointmentType newAppointmentType = null;
            if (newAppointments[i] instanceof Onetime) {
                newAppointmentType = AppointmentBook.appointmentType.Onetime;
            }
            else if (newAppointments[i] instanceof Daily) {
                newAppointmentType = AppointmentBook.appointmentType.Daily;
            }
            else if (newAppointments[i] instanceof Monthly) {
                newAppointmentType = AppointmentBook.appointmentType.Monthly;
            }
            assert newAppointmentType != null;
            assert oldAppointmentType == newAppointmentType;
            assert appointments[i].getTextualDescription().equals(newAppointments[i].getTextualDescription());
            assert appointments[i].getDate().get(Calendar.YEAR) == newAppointments[i].getDate().get(Calendar.YEAR);
            assert appointments[i].getDate().get(Calendar.MONTH) == newAppointments[i].getDate().get(Calendar.MONTH);
            assert appointments[i].getDate().get(Calendar.DAY_OF_MONTH) == newAppointments[i].getDate().get(Calendar.DAY_OF_MONTH);
        }
    }

    /**
     * Fills an Appointment Book with test data
     * @param testBook the AppointmentBook to fill
     */
    private void fillAppointmentBookWithBadData(AppointmentBook testBook) {
        // Create parallel arrays for sample events
        int[] sampleDates = {
            2017,  1, 23,
            2017,  1, 25,
            2017,  1, 30,
            2017,  2,  1,
            2017,  2,  6,
            2017,  2,  1,
            2017,  3,  4,
            2017,  2, 13,
            2017,  2,  7
        };
        String[] sampleDescriptions = {
            "Academic\\\"Fun, \"Work\"",
            "Homewor},k",
            "Monthly Quiz,",
            "(Spring ICE)",
            "Winter Mega Fair?",
            "{Start-of-the-month kickoff}",
            "{Org} meeting",
            "Music\nrehearsal",
            "Group }Project"
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
        assert (sampleDates.length == (sampleDescriptions.length * 3)) && (sampleDates.length == (sampleDateTypes.length * 3)) :
            "There is an inconsistent number of test dates";

        // Add events to AppointmentBook
        GregorianCalendar eventDate;
        for (int i = 2; i < sampleDates.length; i+= 3) {
            eventDate = new GregorianCalendar(sampleDates[i-2], sampleDates[i-1] - 1, sampleDates[i]);
            testBook.addAppointment(sampleDateTypes[i / 3], sampleDescriptions[i / 3], eventDate);
        }
    }
}
