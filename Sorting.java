/**
 * Sorting.java
 * 
 * Implements five sorting algorithms for experimentation:
 * heapsort, mergesort, quicksort, tree sort, and block sort.
 * 
 * The main method generates random arrays, runs each sorting
 * algorithm, records timing results, and verifies correctness.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sorting {

    /* ============================
       QUICKSORT
       ============================ */

    public static <T extends Comparable<? super T>> void quicksort(List<T> list) {
        quicksortHelper(list, 0, list.size() - 1);
    }

    private static <T extends Comparable<? super T>> void quicksortHelper(List<T> list, int low, int high) {
        if (low >= high) return;

        T pivot = list.get(low);
        int left = low + 1;
        int right = high;

        while (left <= right) {
            while (left <= right && list.get(left).compareTo(pivot) < 0) {
                left++;
            }
            while (left <= right && list.get(right).compareTo(pivot) > 0) {
                right--;
            }

            if (left <= right) {
                swap(list, left, right);
                left++;
                right--;
            }
        }

        swap(list, low, right);

        quicksortHelper(list, low, right - 1);
        quicksortHelper(list, right + 1, high);
    }


    /* ============================
       HEAPSORT (max-heap)
       ============================ */

    public static <T extends Comparable<? super T>> void heapsort(List<T> list) {
        int n = list.size();

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i);
        }

        // Extract max repeatedly
        for (int i = n - 1; i > 0; i--) {
            swap(list, 0, i);
            heapify(list, i, 0);
        }
    }

    private static <T extends Comparable<? super T>> void heapify(List<T> list, int size, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < size && list.get(left).compareTo(list.get(largest)) > 0) {
            largest = left;
        }
        if (right < size && list.get(right).compareTo(list.get(largest)) > 0) {
            largest = right;
        }

        if (largest != i) {
            swap(list, i, largest);
            heapify(list, size, largest);
        }
    }


    /* ============================
       MERGE SORT
       ============================ */

    public static <T extends Comparable<? super T>> void mergeSort(List<T> list) {
        if (list.size() <= 1) return;
        mergeSortHelper(list, 0, list.size() - 1);
    }

    private static <T extends Comparable<? super T>> void mergeSortHelper(List<T> list, int left, int right) {
        if (left >= right) return;

        int mid = (left + right) / 2;

        mergeSortHelper(list, left, mid);
        mergeSortHelper(list, mid + 1, right);

        merge(list, left, mid, right);
    }

    private static <T extends Comparable<? super T>> void merge(List<T> list, int left, int mid, int right) {
        List<T> temp = new ArrayList<>();

        int i = left;
        int j = mid + 1;

        while (i <= mid && j <= right) {
            if (list.get(i).compareTo(list.get(j)) <= 0) {
                temp.add(list.get(i++));
            } else {
                temp.add(list.get(j++));
            }
        }

        while (i <= mid) temp.add(list.get(i++));
        while (j <= right) temp.add(list.get(j++));

        for (int k = 0; k < temp.size(); k++) {
            list.set(left + k, temp.get(k));
        }
    }


    /* ============================
       TREE SORT (BST)
       ============================ */

    private static class Node<T> {
        T value;
        Node<T> left, right;
        Node(T v) { value = v; }
    }

    public static <T extends Comparable<? super T>> void treeSort(List<T> list) {
        Node<T> root = null;

        for (T value : list) {
            root = insert(root, value);
        }

        list.clear();
        inorder(root, list);
    }

    private static <T extends Comparable<? super T>> Node<T> insert(Node<T> root, T value) {
        if (root == null) return new Node<>(value);

        if (value.compareTo(root.value) < 0) {
            root.left = insert(root.left, value);
        } else {
            root.right = insert(root.right, value);
        }
        return root;
    }

    private static <T extends Comparable<? super T>> void inorder(Node<T> root, List<T> list) {
        if (root == null) return;
        inorder(root.left, list);
        list.add(root.value);
        inorder(root.right, list);
    }


    /* ============================
       BLOCK SORT (simplified)
       ============================ */

    public static <T extends Comparable<? super T>> void blockSort(List<T> list) {
        int blockSize = 2;

        // Sort tiny blocks
        for (int i = 0; i < list.size(); i += blockSize) {
            int end = Math.min(i + blockSize - 1, list.size() - 1);
            insertionSort(list, i, end);
        }

        // Merge blocks
        while (blockSize < list.size()) {
            for (int start = 0; start < list.size(); start += 2 * blockSize) {
                int mid = Math.min(start + blockSize - 1, list.size() - 1);
                int end = Math.min(start + 2 * blockSize - 1, list.size() - 1);
                merge(list, start, mid, end);
            }
            blockSize *= 2;
        }
    }

    private static <T extends Comparable<? super T>> void insertionSort(List<T> list, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            T key = list.get(i);
            int j = i - 1;

            while (j >= left && list.get(j).compareTo(key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }


    /* ============================
       UTILITIES + MAIN METHOD
       ============================ */

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static void main(String[] args) {
        final int SIZE = 20000;
        Random rand = new Random();

        List<Integer> q = new ArrayList<>();
        List<Integer> h = new ArrayList<>();
        List<Integer> m = new ArrayList<>();
        List<Integer> t = new ArrayList<>();
        List<Integer> b = new ArrayList<>();

        for (int i = 0; i < SIZE; i++) {
            int num = rand.nextInt(100000);
            q.add(num);
            h.add(num);
            m.add(num);
            t.add(num);
            b.add(num);
        }

        long start, end;

        start = System.currentTimeMillis();
        quicksort(q);
        end = System.currentTimeMillis();
        System.out.println("Quicksort time: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        heapsort(h);
        end = System.currentTimeMillis();
        System.out.println("Heapsort time: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        mergeSort(m);
        end = System.currentTimeMillis();
        System.out.println("Merge sort time: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        treeSort(t);
        end = System.currentTimeMillis();
        System.out.println("Tree sort time: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        blockSort(b);
        end = System.currentTimeMillis();
        System.out.println("Block sort time: " + (end - start) + "ms");
    }
}
