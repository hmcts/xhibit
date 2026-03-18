package uk.gov.courtservice.xhibit.client.crestformsbf.util;

import java.util.Comparator;

/**
 * Factory for producing reusable comparator objects
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

public final class ComparatorFactory {

    private static final Comparator normalComparator = new NormalComparator();

    private static final Comparator reverseComparator = new ReverseComparator();

    /**
     * Stop instantiation of this class
     */
    private ComparatorFactory() {
    }

    /**
     * @return a compartor which sorts objects into their natural order
     */
    public static Comparator getNormalComparator() {
        return normalComparator;
    }

    /**
     * @return a compartor which sorts objects into the reverse off their
     *         natural order
     */
    public static Comparator getReverseComparator() {
        return reverseComparator;
    }

    /**
     * Compares two objects using their natural order
     */
    private static class NormalComparator implements Comparator {
        /**
         * Comparator implementation.
         * 
         * @see java.util.Comparator#compare(Object, Object) Comparator
         */
        public int compare(Object o1, Object o2) {
            if (o1 instanceof String && o2 instanceof String) {
                return compare((String) o1, (String) o2);
            } else if (o1 instanceof Boolean && o2 instanceof Boolean) {
                return compare((Boolean) o1, (Boolean) o2);
            } else if (o1 instanceof Comparable && o2 instanceof Comparable) {
                return compare((Comparable) o1, (Comparable) o2);
            } else {
                return 0;
            }
        }

        public int compare(Boolean b1, Boolean b2) {
            return Util.compareTo(b1.booleanValue(), b2.booleanValue());
        }

        public int compare(String s1, String s2) {
            try {
                return Util.compareTo(Double.parseDouble(s1), Double.parseDouble(s2));
            } catch (NumberFormatException nfe) {
                try {
                    return Util.compareTo(Util.parseBoolean(s1), Util.parseBoolean(s2));
                } catch (BooleanFormatException bfe) {
                    return s1.compareTo(s2);
                }
            }
        }

        public int compare(Comparable c1, Comparable c2) {
            return c1.compareTo(c2);
        }
    };

    /**
     * Compares two objects using their natural order reversed
     */
    private static class ReverseComparator extends NormalComparator {
        /**
         * Comparator implementation.
         * 
         * @see java.util.Comparator#compare(Object, Object) Comparator
         */
        public int compare(Object o1, Object o2) {
            return -super.compare(o1, o2);
        }
    };

}
