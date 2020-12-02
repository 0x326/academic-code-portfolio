package edu.miamioh.meyerjm;

import java.util.ArrayList;
import java.util.Scanner;

// Created by John Meyer on 4/27/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab12

/**
 * Represents a person
 */
public class Person implements Comparable {
    // Define instance variable
    private String name;

    /**
     * Creates a Person object
     * @param name The person's name
     */
    public Person(String name) {
        this.name = name;
    } // end constructor Person

    /**
     * Compare the given object to this Person. If the object is a Person, then they are compared by their names.
     * Otherwise, this method will return 0;
     * @param o The object to compare
     * @return Usual comparision notation (always 0 if the given object is not comparable to this Person)
     */
    public int compareTo(Object o) {
        if (o instanceof Person) {
            Person person = (Person) o;
            return name.compareTo(person.toString());
        }
        return 0;
    } // end method compareTo

    /**
     * Returns the name of this Person
     * @return The name of this Person
     */
    @Override
    public String toString() {
        return name;
    } // end method toString

    /**
     * Tests the Comparable implementation of this class with the user's input
     * @param args Commandline arguments
     */
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        ArrayList<Person> personList = new ArrayList<Person>();
        final int NUMBER_OF_NAMES = 10;
        // Get names
        System.out.printf(":: Enter %d names ::%n", NUMBER_OF_NAMES);
        for (int i = 0; i < 10; i++) {
            System.out.printf("Enter person %d: ", i + 1);
            personList.add(new Person(keyboard.nextLine()));
        }
        // Sort and display results
        personList.sort(null);
        System.out.println();
        System.out.printf("First person: %s%n", personList.get(0));
        System.out.printf("Last person: %s%n", personList.get(personList.size() - 1));
    } // end method main
} // end class Person
