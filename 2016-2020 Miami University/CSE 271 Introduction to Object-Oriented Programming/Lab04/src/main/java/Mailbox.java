import java.util.ArrayList;

/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab04
 */
public class Mailbox {
    // Define instance variables
    private final String signature;
    private ArrayList<Message> messageCollection;

    /**
     * Creates a Mailbox object. The signature will be appended to each message it contains
     * @param signature The signature
     */
    public Mailbox(String signature) {
        this.signature = signature;
        this.messageCollection = new ArrayList<>();
    } // end method Mailbox

    /**
     * Adds a message to the Mailbox
     * @param message The message
     */
    public void addMessage(Message message) {
        this.messageCollection.add(message);
        this.messageCollection.get(this.messageCollection.size() - 1).append(signature);
    } // end method addMessage

    /**
     * Gets a message
     * @param i The index of the message (in the container array)
     * @return The specified message
     */
    public Message getMessage(int i) {
        if (i < this.messageCollection.size()) {
            return this.messageCollection.get(i);
        }
        else {
            return null;
        }
    } // end method getMessage

    /**
     * Removes a message
     * @param i The index of the message (in the container array)
     */
    public void removeMessage(int i) {
        if (i < this.messageCollection.size()) {
            this.messageCollection.remove(i);
        }
    } // end method removeMessage

    // Define getters
    public ArrayList<Message> getMessageCollection() {
        return messageCollection;
    }
} // end class Mailbox
