/**
 * John Meyer
 * 2016/01/26
 * CSE 271 F
 * Dr. Angel Bravo
 */

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author meyerjm
 * Lab 01 Project
 */
public class Lab_01_Tester {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int[] array = Lab_01_ArrayUtilities.buildIntArray(20, 10, 29);
		PrintWriter fileOut = new PrintWriter(new File("Lab_01_nums.txt"));
		printArray(array, fileOut);
		
		// Count odd numbers
		int numOfOddNumbers = 0;
		for (int num : array) {
			numOfOddNumbers += num % 2 == 1 ? 1 : 0;
		}
		
		// Swap evens indices
		for (int i = 1; i < array.length; i += 2) {
			Lab_01_ArrayUtilities.swap(array, i, i - 1);
		}
		printArray(array, fileOut);
		
		// Sort array
		Arrays.sort(array);
		printArray(array, fileOut);

		// Close file 
		fileOut.close();
		
		// Open words.txt
		Scanner fileIn = new Scanner(new File("words.txt"));
		int numOfVowels = 0;
		int numOfConsonants = 0;
		int numOfOthers = 0;
		while(fileIn.hasNext()) {
			String word = fileIn.nextLine().toLowerCase();
			for (char letter : word.toCharArray()) {
				if (!Character.isLetter(letter)) {
					numOfOthers++;
				}
				else if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') {
					numOfVowels++;
				}
				else {
					numOfConsonants++;
				}
			}
		}
		// Close file
		fileIn.close();

		// Report info
		System.out.printf("Odd values: %d%n", numOfOddNumbers);
		System.out.printf("%d vowels, %d consonants, %d other characters%n", numOfVowels, numOfConsonants, numOfOthers);
		
	} // End main
	
	public static void printArray(int[] array, PrintWriter fileOut) {
		System.out.print("[" + array[0]);
		fileOut.print("[" + array[0]);
		for (int i = 1; i < array.length; i++) {
			System.out.print(", " + array[i]);
			fileOut.print(", " + array[i]);
		}
		System.out.print("]\n");
		fileOut.print("]\n");
	} // End printArray

} // End Lab_01_Tester Class
