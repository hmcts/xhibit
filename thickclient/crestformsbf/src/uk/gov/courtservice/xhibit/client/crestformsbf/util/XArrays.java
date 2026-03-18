package uk.gov.courtservice.xhibit.client.crestformsbf.util;

import java.util.Comparator;

/**
 * Provide array sorting beyond java.util.Arrays (a lot of this code is modified
 * from java.util.Arrays)
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public final class XArrays {

    /**
     * Prevent this utility class from being neadlessly instantiated
     */
    private XArrays() {
    }

    /**
     * Return an array containing the indexes of the objects sorted using the
     * Objects natural order (implementation of Comparable interface)
     * 
     * @param objects
     *            the objects to get the sorted indicies off
     * @return the sorted indicies
     */
    public static int[] getSortedIndicies(Object[] objects) {
        return getSortedIndicies(objects, ComparatorFactory.getNormalComparator());
    }

    /**
     * Return an array containing the indexes of the objects sorted using the
     * comparators algorithm.
     * 
     * @param objects
     *            the objects to get the sorted indicies off
     * @param comparator
     *            the algorithm to use
     * @return the sorted indicies
     */
    public static int[] getSortedIndicies(Object[] objects, Comparator comparator) {
        if (objects == null) {
            return null;
        } else if (objects.length == 0) {
            return new int[0];
        } else {
            int[] indicies = new int[objects.length];
            for (int i = 0; i < objects.length; i++) {
                indicies[i] = i;
            }
            sortIndicies(objects, indicies, comparator);
            return indicies;
        }
    }

    /**
     * Return an array containing the indexes of the objects sorted using the
     * Objects natural order (implementation of Comparable interface)
     * 
     * @param objects
     *            the objects to get the sorted indicies off
     * @return the sorted indicies
     */
    public static int[] getSortedIndicies(XArray objects) {
        return getSortedIndicies(objects, ComparatorFactory.getNormalComparator());
    }

    /**
     * Return an array containing the indexes of the objects sorted using the
     * comparators algorithm.
     * 
     * @param objects
     *            the objects to get the sorted indicies off
     * @param comparator
     *            the algorithm to use
     * @return the sorted indicies
     */
    public static int[] getSortedIndicies(XArray objects, Comparator comparator) {
        if (objects == null) {
            return null;
        } else if (objects.length() == 0) {
            return new int[0];
        } else {
            int[] indicies = new int[objects.length()];
            for (int i = 0, l = objects.length(); i < l; i++) {
                indicies[i] = i;
            }
            sortIndicies(objects, indicies, comparator);
            return indicies;
        }
    }

    /**
     * Return an array containing the identity indicies a[i] = i
     * 
     * @param length
     *            the length of the new array
     * @return an array populated wityh identity mappings
     */
    public static int[] getIdentityIndicies(int length) {
        int[] indicies = new int[length];
        for (int i = 0; i < length; i++) {
            indicies[i] = i;
        }
        return indicies;
    }

    /**
     * Sort the indicies of the objects using the supplied algorithm
     * 
     * @param objects
     *            the Objects the indicies index
     * @param indicies
     *            the indicies of the Objects
     * @param comparator
     *            the algorithm to use
     */
    public static void sortIndicies(XArray objects, int[] indicies, Comparator comparator) {
        if (objects == null || indicies == null || comparator == null || objects.length() != indicies.length) {
            throw new IllegalArgumentException();
        }
        mergeSortIndicies(objects, (int[]) indicies.clone(), indicies, 0, indicies.length, comparator);
    }

    /**
     * Sort the indicies of the objects using the supplied algorithm
     * 
     * @param objects
     *            the Objects the indicies index
     * @param indicies
     *            the indicies of the Objects
     * @param comparator
     *            the algorithm to use
     */
    public static void sortIndicies(Object[] objects, int[] indicies, Comparator comparator) {
        if (objects == null || indicies == null || comparator == null || objects.length != indicies.length) {
            throw new IllegalArgumentException();
        }
        mergeSortIndicies(new ObjectXArray(objects), (int[]) indicies.clone(), indicies, 0, indicies.length, comparator);
    }

    private static void mergeSortIndicies(XArray objects, int src[], int dest[], int low, int high, Comparator c) {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < 7) {
            for (int i = low; i < high; i++)
                for (int j = i; j > low && c.compare(objects.get(dest[j - 1]), objects.get(dest[j])) > 0; j--)
                    swap(dest, j, j - 1);
            return;
        }

        // Recursively sort halves of dest into src
        int mid = (low + high) >> 1;
        mergeSortIndicies(objects, dest, src, low, mid, c);
        mergeSortIndicies(objects, dest, src, mid, high, c);

        // If list is already sorted, just copy from src to dest. This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare(objects.get(src[mid - 1]), objects.get(src[mid])) <= 0) {
            System.arraycopy(src, low, dest, low, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = low, p = low, q = mid; i < high; i++) {
            if (q >= high || p < mid && c.compare(objects.get(src[p]), objects.get(src[q])) <= 0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }

    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(int x[], int a, int b) {
        int t = x[a];
        x[a] = x[b];
        x[b] = t;
    }

    /**
     * Check that fromIndex and toIndex are in range, and throw an appropriate
     * exception if they aren't.
     */
    private static void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        if (fromIndex < 0)
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        if (toIndex > arrayLen)
            throw new ArrayIndexOutOfBoundsException(toIndex);
    }

}
