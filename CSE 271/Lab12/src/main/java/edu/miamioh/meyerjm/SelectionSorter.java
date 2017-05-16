package edu.miamioh.meyerjm;

// Created by John Meyer on 4/27/2017.
// CSE 271 F
// Dr. Angel Bravo
// Lab12

/**
 * Implements a selection sort
 */
public class SelectionSorter {
    /**
     * Sorts the given array with a selection sort
     * @param array The array to sort
     */
    public static void sort(int[] array) {
        for (int i = 0; i < array.length; i++) {
            // Find largest
            int indexOfLargestElement = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] > array[indexOfLargestElement]) {
                    indexOfLargestElement = j;
                }
            }
            // Swap values (via an instructor-given library)
            ArrayUtil.swap(array, i, indexOfLargestElement);
        }
    } // end method sort
} // end class SelectionSorter
