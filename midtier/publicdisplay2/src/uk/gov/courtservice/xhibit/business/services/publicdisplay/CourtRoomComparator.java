package uk.gov.courtservice.xhibit.business.services.publicdisplay;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;

/**
 * <p>
 * Title: Comparator used to sort court rooms.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Initial naive implementation that sorts purely based on the court room name.
 * Implemented as a 'factory' style singleton so that we can easily change
 * implementation in the future.
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

public class CourtRoomComparator implements Comparator {
    private static final CourtRoomComparator _instance = new CourtRoomComparator();

    private CourtRoomComparator() {
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
     * Compares two instances of <code>XhbCourtRoom</code> in order to provide
     * a natural order based on their CREST court room numbers.
     * 
     * @param o1
     *            First instance of <code>XhbCourtRoom</code> to compare.
     * @param o2
     *            Second instance of <code>XhbCourtRoom</code> to compare
     * @return as per spec of Comparator interface.
     * @throws ClassCastException
     *             If an object not of type <code>XhbCourtRoom</code> is
     *             passed.
     */
    public int compare(Object o1, Object o2) throws ClassCastException {
        return ((XhbCourtRoom) o1).getCrestCourtRoomNo().compareTo(((XhbCourtRoom) o2).getCrestCourtRoomNo());
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