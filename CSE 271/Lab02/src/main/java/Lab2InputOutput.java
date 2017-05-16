// John Meyer
// CSE 271 F
// Dr. Angel Bravo

import java.util.Scanner;
import java.io.*;

/**
 * Copies a file with line numbers prefixed to every line
 */
public class Lab2InputOutput {
    public static void main(String[] args) throws Exception {
        // Define variables
        Scanner keyboardReader = new Scanner(System.in);
        String inputFileName;
        String outputFileName;
        // Check arguments
        if (args.length == 0) {
            System.out.println("Usage: java Lab2InputOutput /path/to/file");
            return;
        }
        inputFileName = args[0];
        // Find input file
        File inputFile = new File(inputFileName);
        Scanner fileInput = new Scanner(inputFile);

        // Get output file name
        System.out.print("Output File Name: ");
        outputFileName = keyboardReader.next();
        File outputFile = new File(outputFileName);

        // Start copying
        PrintWriter fileOutput = new PrintWriter(outputFile);
        String lineContent;
        for (int lineNumber = 1; fileInput.hasNext(); lineNumber++) {
            lineContent = fileInput.nextLine();
            fileOutput.printf("/* %d */ %s%n", lineNumber, lineContent);
        }
        fileInput.close();
        fileOutput.close();
    } // end method main
} // end class Lab2InputOutput
