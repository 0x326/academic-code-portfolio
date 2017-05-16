import java.util.ArrayList;

/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab04
 */
public class MailboxTester {
    public static void main(String[] args) {
        // Declare testing variables
        Mailbox testMailbox;
        Message resultingMessage;
        Message expectedMessage;
        String resultingString;
        String expectedString;
        ArrayList<Message> resultingList;
        ArrayList<Message> expectedList;

        // Create sample Messages for testing
        Message sampleMessage1 = new Message("John Doe", "Alice");
        sampleMessage1.append("Hello World!");
        Message sampleMessage2 = new Message("John Doe", "Bob");
        sampleMessage2.append("Hello there!");
        Message sampleMessage3 = new Message("John Doe", "Joe");
        sampleMessage3.append("Well, hello there!");

        // Test signature
        testMailbox = new Mailbox("John Doe");
        testMailbox.addMessage(sampleMessage1);
        resultingString = testMailbox.getMessage(0).toString();
        expectedString = "From: John Doe\nTo: Alice\nHello World!\nJohn Doe\n";
        displayStringTestingResults("signature", resultingString, expectedString);

        // Test add message
        testMailbox.addMessage(sampleMessage2);
        testMailbox.addMessage(sampleMessage3);
        resultingList = testMailbox.getMessageCollection();
        expectedList = new ArrayList<Message>();
        expectedList.add(sampleMessage1);
        expectedList.add(sampleMessage2);
        expectedList.add(sampleMessage3);
        displayArrayListTestingResults("add message", resultingList, expectedList);

        // Test get message
        resultingMessage = testMailbox.getMessage(1);
        expectedMessage = sampleMessage2;
        displayMessageTestingResults("add/get message", resultingMessage, expectedMessage);

        // Test remove message
        testMailbox.removeMessage(0);
        resultingMessage = testMailbox.getMessage(0);
        expectedMessage = sampleMessage2;
        displayMessageTestingResults("remove message", resultingMessage, expectedMessage);
    } // end method main

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
            System.out.println("Note: Test yielded unexpected output");
        }
        System.out.println();
    } // end method displayStringTestingResults

    /**
     * Displays the result of a Message-based test
     * @param testDescription A description of the test
     * @param actualResult The result of the test
     * @param expectedResult The expected result of the test
     */
    public static void displayMessageTestingResults(String testDescription, Message actualResult, Message expectedResult) {
        System.out.println("====================");
        System.out.println("Testing:  " + testDescription);
        System.out.println("Result:   " + actualResult.toString());
        System.out.println("Expected: " + expectedResult.toString());
        if (actualResult != expectedResult) {
            System.out.println("Note: Test yielded unexpected output");
        }
        System.out.println();
    } // end method displayMessageTestingResults

    /**
     * Displays the result of a ArrayList-based test
     * @param testDescription A description of the test
     * @param actualResult The result of the test
     * @param expectedResult The expected result of the test
     */
    public static void displayArrayListTestingResults(String testDescription, ArrayList<Message> actualResult,
                                                      ArrayList<Message> expectedResult) {
        System.out.println("====================");
        System.out.println("Testing:  " + testDescription);
        System.out.println("Result:   " + actualResult.toString());
        System.out.println("Expected: " + expectedResult.toString());

        boolean isDifferent = actualResult.size() != expectedResult.size();
        for (int i = 0; i < actualResult.size() && !isDifferent; i++) {
            if (actualResult.get(i) != expectedResult.get(i)) {
                isDifferent = true;
            }
        }
        if (isDifferent) {
            System.out.println("Note: Test yielded unexpected output");
        }
        System.out.println();
    } // end method displayArrayListTestingResults
} // end class MailboxTester
