/**
 * John Meyer
 * 2016/01/26
 * CSE 271 F
 * Dr. Angel Bravo
 */

/**
 * @author meyerjm
 *
 */
public class Lab_01_ArrayUtilities {

	/**
	 * Creates an array of random integers within an inclusive range
	 * @param length The length of the array which to create
	 * @param fromNum The lowest inclusive value for the array
	 * @param toNum The highest inclusive value for the array
	 */
	public static int[] buildIntArray(int length, int fromNum, int toNum) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = (int) ((toNum - fromNum + 1) * Math.random()) + fromNum;
		}
		return array;
	} // End buildIntArray
	
	/**
	 * Swaps the values at the given indices in the given array
	 * @param nums The array on which to operate
	 * @param i The index of one element to swap
	 * @param j The index of another element to swap
	 */
	public static void swap(int[] nums, int i, int j) {
		int formerNum = nums[i];
		nums[i] = nums[j];
		nums[j] = formerNum;
	} // End swap
} // End Lab_01_ArrayUtilities class
