// Created by John Meyer on 3/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab07-08

package edu.miamioh.meyerjm;

/**
 * Represents a person
 */
public class Person implements Comparable {
    // Define instance variables
    private String uniqueIdentifier;
    private String emailAddress;

    /**
     * Creates a Person
     * @param uniqueIdentifier A unique ID for this person
     * @param emailAddress An email address for this person
     */
    public Person(String uniqueIdentifier, String emailAddress) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.emailAddress = emailAddress.toLowerCase();
    } // end constructor Person

    /**
     * Gets this Person's email address
     * @return The email address
     */
    public String getEmailAddress() {
        return emailAddress;
    } // end method getEmailAddress

    /**
     * Gets a String representation of this Person
     * @return The String representation
     */
    @Override
    public String toString() {
        return "Person{" +
            "uniqueIdentifier='" + uniqueIdentifier + '\'' +
            '}';
    } // end method toString

    /**
     * Evaluates this.toString().compareTo(object.toString())
     * @param object The other object with which to compare
     */
    public int compareTo(Object object) {
        return this.toString().compareTo(object.toString());
    } // end method compareTo
} // end class Person
