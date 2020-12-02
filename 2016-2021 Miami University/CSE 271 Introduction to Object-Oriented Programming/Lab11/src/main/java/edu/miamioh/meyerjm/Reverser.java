package edu.miamioh.meyerjm;

// Created by John Meyer on 4/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab11
public class Reverser {
    /**
     * Returns the reverse of the given String
     * @param text The String to reverse
     * @return The reverse of the String
     */
    public static String reverse(String text) {
        if (text.length() <= 1) {
            return text;
        }
        else {
            return text.substring(text.length() - 1) + reverse(text.substring(0, text.length() - 1));
        }
    }

    /**
     * Tests the reverse method
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        String [][] testCases = {
            { "Hello!", "!olleH" },
            { "Star Wars would be nothing would Jar Jar Binks", "skniB raJ raJ dluow gnihton eb dluow sraW ratS" },
            { "%$!#$@#!", "!#@$#!$%"},
            { "Able was I ere I saw Elba", "ablE was I ere I saw elbA" },
            { "1", "1" },
            { "", "" }
        };
        for (String[] testCase : testCases) {
            System.out.printf("\"%s\" (expected \"%s\")%n", reverse(testCase[0]), testCase[1]);
        }
    }
}
