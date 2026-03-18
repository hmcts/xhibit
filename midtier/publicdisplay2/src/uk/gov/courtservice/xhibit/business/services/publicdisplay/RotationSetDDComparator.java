package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.entities.xhb_rotation_set_dd.XhbRotationSetDd;

/**
 * <p>
 * Title: Comparator used to sort <code>RotationSetDisplayDocuments</code>
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * This comparator sorts arrays of <code>RotationSetDisplayDocuments</code> by
 * their 'Ordering' field.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class RotationSetDDComparator implements Comparator {
    private static final RotationSetDDComparator _instance = new RotationSetDDComparator();

    private RotationSetDDComparator() {
    }

    /**
     * Get a comparator to sort court rooms.
     * 
     * @return A class implementing Comparator able to sort by
     *         <code>XhbCourtRoom</code>.
     */
    public static final Comparator getInstance() {
        return _instance;
    }

    /**
     * Compares two instances of <code>XhbRotationSetDd</code> in order to
     * provide a natural order based on their ordering fields.
     * 
     * @param o1
     *            First instance of <code>XhbRotationSetDd</code> to compare.
     * @param o2
     *            Second instance of <code>XhbRotationSetDd</code> to compare
     * @return as per spec of Comparator interface.
     * @throws ClassCastException
     *             If an object not of type <code>XhbRotationSetDd</code> is
     *             passed.
     */
    public int compare(Object o1, Object o2) throws ClassCastException {
        return ((XhbRotationSetDd) o1).getOrdering().compareTo(((XhbRotationSetDd) o2).getOrdering());
    }

    /**
     * Checks whether the object passed in is equivalent to this class.
     * 
     * @param obj
     *            The object check equivalence on.
     * @return true if equivalent.
     */
    public boolean equals(Object obj) {
        return this == obj;
    }
}