import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sorting.java
 * 
 * Author: Greg Hernandez
 * Date: November 9, 2025
 * 
 * Description:
 * Implements two in-place sorting algorithms — quicksort and heapsort —
 * for generic Lists in Java. Both methods return the number of element-to-element
 * comparisons made using compareTo. This program also demonstrates sorting
 * performance using 20,000 random integers.
 * 
 * Assignment: Priority Queues (via Heaps) for Sorting — Compsci 2
 */


/**
 * The Sorting class provides static methods for quicksort and heapsort.
 * Both algorithms operate in-place and return the number of comparisons made.
 */
public class Sorting {

    /**
     * Sorts the given list in-place using the quicksort algorithm.
     * The first element of each partition is used as the pivot.
     *
     * @param <T>  the element type, which must implement Comparable
     * @param list the list to sort in-place
     * @return the number of element comparisons performed
     */
    public static <T extends Comparable<? super T>> int quicksort(List<T> list) {
        return quicksortHelper(list, 0, list.size() - 1);
    }

    /**
     * Recursive helper method for quicksort.
     *
     * @param <T>  the element type
     * @param list the list being sorted
     * @param low  the starting index of the sublist
     * @param high the ending index of the sublist
     * @return the number of comparisons made in this recursion
     */
    private static <T extends Comparable<? super T>> int quicksortHelper(List<T> list, int low, int high) {
        if (low >= high) {
            return 0;
        }

        int comparisons = 0;
        T pivot = list.get(low);
        int left = low + 1;
        int right = high;

        // Partition the list around the pivot
        while (left <= right) {
            while (left <= right && list.get(left).compareTo(pivot) < 0) {
                left++;
                comparisons++;
            }
            while (left <= right && list.get(right).compareTo(pivot) > 0) {
                right--;
                comparisons++;
            }

            if (left <= right) {
                swap(list, left, right);
                left++;
                right--;
            }
        }

        swap(list, low, right); // Move pivot into correct position

        // Recurse into partitions
        comparisons += quicksortHelper(list, low, right - 1);
        comparisons += quicksortHelper(list, right + 1, high);

        return comparisons;
    }

    /**
     * Sorts the given list in-place using the heapsort algorithm.
     * The algorithm constructs a max-heap and repeatedly extracts the maximum element.
     *
     * @param <T>  the element type, which must implement Comparable
     * @param list the list to sort in-place
     * @return the number of element comparisons performed
     */
    public static <T extends Comparable<? super T>> int heapsort(List<T> list) {
        int n = list.size();
        int comparisons = 0;

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            comparisons += heapify(list, n, i);
        }

        // Extract elements one by one
        for (int i = n - 1; i > 0; i--) {
            swap(list, 0, i); // Move max element to the end
            comparisons += heapify(list, i, 0);
        }

        return comparisons;
    }

    /**
     * Maintains the max-heap property for the subtree rooted at index i.
 * @param <T>  the element type
     * @param list the list representing the heap
     * @param size the effective heap size
     * @param i    the index of the root of the subtree
     * @return the number of comparisons performed during heapify
     */
    private static <T extends Comparable<? super T>> int heapify(List<T> list, int size, int i) {
        int comparisons = 0;
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Compare left child
        if (left < size) {
            comparisons++;
            if (list.get(left).compareTo(list.get(largest)) > 0) {
                largest = left;
            }
        }

        // Compare right child
        if (right < size) {
            comparisons++;
            if (list.get(right).compareTo(list.get(largest)) > 0) {
                largest = right;
            }
        }

        // If largest is not root, swap and continue heapifying
        if (largest != i) {
            swap(list, i, largest);
            comparisons += heapify(list, size, largest);
        }

        return comparisons;
    }

    /**
     * Swaps two elements in the list.
     *
     * @param <T> the element type
     * @param list the list whose elements are to be swapped
     * @param i index of the first element
     * @param j index of the second element
     */
    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    /**
     * Main method for testing and comparing quicksort and heapsort.
     * Generates two identical lists of 20,000 random integers, sorts one with
     * quicksort and one with heapsort, verifies the sorting, and prints comparison counts.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        final int SIZE = 20000;
        Random rand = new Random();

        // Create two identical lists
        List<Integer> quickList = new ArrayList<>(SIZE);
        List<Integer> heapList = new ArrayList<>(SIZE);

        for (int i = 0; i < SIZE; i++) {
            int num = rand.nextInt(100000);
            quickList.add(num);
            heapList.add(num);
        }

        // Perform sorting
        int quickComparisons = quicksort(quickList);
        int heapComparisons = heapsort(heapList);

        // Verify sorted order
        boolean quickSorted = isSorted(quickList);
        boolean heapSorted = isSorted(heapList);

        // Output results
        System.out.println("QuickSort comparisons: " + quickComparisons + " (sorted: " + quickSorted + ")");
        System.out.println("HeapSort comparisons:  " + heapComparisons + " (sorted: " + heapSorted + ")");
    }
       /**
     * Verifies whether a list is sorted in non-decreasing order.
     *
     * @param <T>  the element type
     * @param list the list to check
     * @return true if sorted, false otherwise
     */
    private static <T extends Comparable<? super T>> boolean isSorted(List<T> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(list.get(i - 1)) < 0) {
                return false;
            }
        }
        return true;
    }
}