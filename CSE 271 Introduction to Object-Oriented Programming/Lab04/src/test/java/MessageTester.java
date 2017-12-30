/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab04
 */
public class MessageTester {
    public static void main(String args[]) {
        // Declare testing variables
        Message testMessage;
        String testResult;
        String expectedResult;

        // Test sender
        testMessage = new Message("Alice", "Bob");
        testResult = testMessage.getSender();
        expectedResult = "Alice";
        displayTestingResults("Message sender", testResult, expectedResult);

        // Test recipient
        testMessage = new Message("Bob", "Alice");
        testResult = testMessage.getRecipient();
        expectedResult = "Alice";
        displayTestingResults("Message recipient", testResult, expectedResult);

        // Test body
        testMessage = new Message("Alice", "Bob");
        testResult = testMessage.getBody();
        expectedResult = "";
        displayTestingResults("Message body", testResult, expectedResult);

        testMessage.append("Hello world!");
        testResult = testMessage.getBody();
        expectedResult = "Hello world!\n";
        displayTestingResults("Message body", testResult, expectedResult);

        testMessage.append("It's a wonderful day!");
        testResult = testMessage.getBody();
        expectedResult = "Hello world!\nIt's a wonderful day!\n";
        displayTestingResults("Message body", testResult, expectedResult);

        // Test toString
        testMessage = new Message("Alice", "Bob");
        testMessage.append("To whom it may concern,");
        testMessage.append("The coffee machine is currently broken");
        testMessage.append("Sincerely,");
        testMessage.append("Alice");
        testResult = testMessage.toString();
        expectedResult = "From: Alice\nTo: Bob\nTo whom it may concern,\nThe coffee machine is currently broken\n"
                + "Sincerely,\nAlice\n";
        displayTestingResults("toString", testResult, expectedResult);
    } // end method main

    /**
     * Prints the results of a test
     * @param testDescription A description of the test
     * @param actualResult The result of the test
     * @param expectedResult The expected result of the test
     */
    public static void displayTestingResults(String testDescription, String actualResult, String expectedResult) {
        System.out.println("====================");
        System.out.println("Testing:  " + testDescription);
        System.out.println("Result:   " + actualResult);
        System.out.println("Expected: " + expectedResult);
        if (!actualResult.equals(expectedResult)) {
            System.out.println("Note: Test yielded unexpected output");
        }
        System.out.println();
    } // end method displayTestingResults
} // end class MessageTester
