package edu.miamioh.meyerjm;

import java.util.Arrays;

// Created by John Meyer on 4/20/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab11
public class RecursiveFinder {
    /**
     * Returns the maximum value of a given array
     * @param arr The array in which to find the maximum
     * @return The maximum value of the array
     */
    public static int largestElement(int[] arr) {
        if (arr.length == 0) {
            return 0;
        }
        else if (arr.length == 1) {
            return arr[0];
        }
        else {
            return Math.max(largestElement(Arrays.copyOf(arr, arr.length - 1)), arr[arr.length - 1]);
        }
    }

    /**
     * Tests the reverse method
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        int[][] testCases = {
            { 5, 4, 3, 2, 1 },
            { 5, 4, 3, 2, 1, 7, 0, 25, 49, 21 },
            { 1, 1, 7, 1, 1 },
            { 5 },
            {  }
        };
        int[] expectedValues = {
            5,
            49,
            7,
            5,
            0
        };
        for (int testNumber = 0; testNumber < testCases.length; testNumber++) {
            System.out.printf("%d (expected %d)%n", largestElement(testCases[testNumber]), expectedValues[testNumber]);
        }
    }
}
