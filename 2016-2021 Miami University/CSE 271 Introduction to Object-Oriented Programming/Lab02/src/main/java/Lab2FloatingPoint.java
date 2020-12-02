// John Meyer
// CSE 271 F
// Dr. Angel Bravo

import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Accepts many floating point numbers until two non-numbers are entered consecutively.
 * Afterwards, prints the total of the valid numbers
 */
public class Lab2FloatingPoint {
    public static void main(String[] args) {
        // Define variables
        Scanner keyboardReader = new Scanner(System.in);
        float sumOfNumbers = 0.0f;
        boolean doAsk = true;
        boolean userWrongLastTime = false;

        System.out.println("Begin typing numbers followed by [Enter]");
        System.out.println("Program will quit after two consecutive non-numbers");
        while (doAsk) {
            try {
                // Get user input
                float userInput = keyboardReader.nextFloat();
                sumOfNumbers += userInput;
                userWrongLastTime = false;
            }
            catch (InputMismatchException e) {
                // Catch mismatch error
                if (userWrongLastTime) {
                    doAsk = false;
                }
                String badInput = keyboardReader.next();
                userWrongLastTime = true;
            }
        }
        // Print total
        System.out.printf("Final total is %f%n", sumOfNumbers);
    } // end method main
} // end class Lab2FloatingPoint
