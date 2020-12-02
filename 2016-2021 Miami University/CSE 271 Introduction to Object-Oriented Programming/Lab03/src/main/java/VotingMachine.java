/**
 * John Meyer
 * CSE 271
 * Dr. Angel Bravo
 * Lab03
 */
public class VotingMachine {

    // Define instance variables
    private long republicanVotes;
    private long democratVotes;

    /**
     * Gets the number of Republican votes
     * @return The number of Republicans votes
     */
    public long getRepublicanVotes() {
        return republicanVotes;
    }

    /**
     * Gets the number of Democrat votes
     * @return The number of Democrat votes
     */
    public long getDemocratVotes() {
        return democratVotes;
    }

    /**
     * Reset poll counts to zero
     */
    public void clearState() {
        republicanVotes = 0;
        democratVotes = 0;
    }

    /**
     * Adds a Democrat vote
     */
    public void voteDemocrat() {
        democratVotes++;
    }

    /**
     * Adds a Republican vote
     */
    public void voteRepublican() {
        republicanVotes++;
    }

    // Define constructors
    public VotingMachine() {
        republicanVotes = 0;
        democratVotes = 0;
    }

    public VotingMachine(long republicanVotes, long democratVotes) {
        this.republicanVotes = republicanVotes;
        this.democratVotes = democratVotes;
    }
}
