/**
 * Created by John Meyer on 2/23/2017.
 * CSE 271
 * Dr. Angel Bravo
 * Lab05
 */
public class Tester {
    public static void main(String[] args) {
        // Begin testing
        testPart1();
        testPart2();
    } // end method main

    /**
     * Tests part 1
     */
    public static void testPart1() {
        // Define variables
        String expectedString;
        String actualString;
        double expectedDouble;
        double actualDouble;

        // Test Person
        Person testPerson;

        expectedString = "John Doe";
        testPerson = new Person(expectedString, "2017-02-23");
        actualString = testPerson.getName();
        displayStringTestingResults("Person Constructor: name", actualString, expectedString);

        expectedString = "2017-02-23";
        testPerson = new Person("John Doe", expectedString);
        actualString = testPerson.getDateOfBirth();
        displayStringTestingResults("Person Constructor: dateOfBirth", actualString, expectedString);

        expectedString = "Person{name='John Doe', dateOfBirth='2017-02-23'}";
        testPerson = new Person("John Doe", "2017-02-23");
        actualString = testPerson.toString();
        displayStringTestingResults("Person.toString", actualString, expectedString);

        // Test Student
        Student testStudent;

        expectedString = "Computer Science";
        testStudent = new Student("John Doe", "2017-02-23", expectedString);
        actualString = testStudent.getMajor();
        displayStringTestingResults("Student Constructor: major", actualString, expectedString);

        expectedString = "Student{major='Computer Engineering'} Person{name='John Doe', dateOfBirth='2017-02-23'}";
        testStudent = new Student("John Doe", "2017-02-23", "Computer Engineering");
        actualString = testStudent.toString();
        displayStringTestingResults("Student.toString", actualString, expectedString);

        // Test Instructor
        Instructor testInstructor;

        expectedDouble = 10;
        testInstructor = new Instructor("John Doe", "2017-02-23", expectedDouble);
        actualDouble = testInstructor.getSalary();
        displayDoubleTestingResults("Instructor Constructor: salaray", actualDouble, expectedDouble);

        expectedString = "Instructor{salary=25.55} Person{name='John Doe', dateOfBirth='2017-02-23'}";
        testInstructor = new Instructor("John Doe", "2017-02-23", 25.55);
        actualString = testInstructor.toString();
        displayStringTestingResults("Instructor.toString", actualString, expectedString);
    } // end method testPart1

    /**
     * Tests part 2
     */
    public static void testPart2() {
        // Define variables
        String expectedString;
        String actualString;
        double expectedDouble;
        double actualDouble;

        // Test Employee
        Employee testEmployee;

        testEmployee = new Employee("John Doe", 19);
        expectedString = "John Doe";
        actualString = testEmployee.getName();
        displayStringTestingResults("Employer Constructor: name", actualString, expectedString);

        expectedDouble = 19;
        actualDouble = testEmployee.getSalary();
        displayDoubleTestingResults("Employer Constructor: salary", actualDouble, expectedDouble);

        expectedString = "Employee{name='John Doe', salary=19.00}";
        actualString = testEmployee.toString();
        displayStringTestingResults("Employer.toString", actualString, expectedString);

        // Test Manager
        Manager testManager;

        testManager = new Manager("John Doe", 15.65, "CSE");

        expectedString = "CSE";
        actualString = testManager.getDepartment();
        displayStringTestingResults("Manager Constructor: department", actualString, expectedString);

        expectedString = "Manager{department='CSE'} Employee{name='John Doe', salary=15.65}";
        actualString = testManager.toString();
        displayStringTestingResults("Manager.toString", actualString, expectedString);

        // Test Executive
        Executive testExecutive = new Executive("John Doe", 50.01, "CSE", "Oxford, OH");

        expectedString = "Oxford, OH";
        actualString = testExecutive.getOfficeLocation();
        displayStringTestingResults("Executive Constructor: officeLocation", actualString, expectedString);

        expectedString = "Executive{officeLocation='Oxford, OH'} Manager{department='CSE'} Employee{name='John Doe', salary=50.01}";
        actualString = testExecutive.toString();
        displayStringTestingResults("Executive.toString", actualString, expectedString);

    } // end method testPart2

    /**
     * Displays the result of a String-based test
     * @param testDescription A description of the test
     * @param actualResult The result of the test
     * @param expectedResult The expected result of the test
     */
    public static void displayStringTestingResults(String testDescription, String actualResult, String expectedResult) {
        System.out.println("====================");
        System.out.println("Testing:  " + testDescription);
        System.out.println("Result:   " + actualResult);
        System.out.println("Expected: " + expectedResult);
        if (!actualResult.equals(expectedResult)) {
            System.out.println("!! Note: Test yielded unexpected output");
        }
        System.out.println();
    } // end method displayStringTestingResults

    /**
     * Displays the result of a double-based test
     * @param testDescription A description of the test
     * @param actualResult The result of the test
     * @param expectedResult The expected result of the test
     */
    public static void displayDoubleTestingResults(String testDescription, double actualResult, double expectedResult) {
        System.out.println("====================");
        System.out.println("Testing:  " + testDescription);
        System.out.println("Result:   " + actualResult);
        System.out.println("Expected: " + expectedResult);
        if (Math.abs(actualResult - expectedResult) > 1e-5) {
            System.out.println("!! Note: Test yielded unexpected output");
        }
        System.out.println();
    } // end method displayStringTestingResults
} // end class Tester
