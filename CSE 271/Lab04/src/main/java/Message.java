/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab04
 */
public class Message {
    // Define static variables
    private static final String from = "From: ";
    private static final String to = "\nTo: ";
    // Define instance variables
    private String recipient;
    private String sender;
    private String body;

    /**
     * Creates a Message object
     * @param sender The sender of the message
     * @param recipient The recipient of the message
     */
    public Message(String sender, String recipient) {
        this.recipient = recipient;
        this.sender = sender;
        this.body = "";
    } // end method Message

    /**
     * Appends the string to the message body, which is initially of length zero
     * @param message The message
     */
    public void append(String message) {
        this.body += message + "\n";
    } // end method append

    /**
     * Returns a compiled version of the message, with appropriate headers
     * @return The compiled message
     */
    @Override
    public String toString() {
        return from + sender + to + recipient + "\n" + body;
    } // end method toString

    // Define getters
    public String getRecipient() {
        return recipient;
    } // end method getRecipient

    public String getSender() {
        return sender;
    } // end method getSender

    public String getBody() {
        return body;
    } // end method getBody
} // end class Message
