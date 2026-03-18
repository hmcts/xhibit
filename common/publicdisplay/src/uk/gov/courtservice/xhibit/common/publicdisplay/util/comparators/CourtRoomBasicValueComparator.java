package uk.gov.courtservice.xhibit.common.publicdisplay.util.comparators;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;

/**
 * <p>
 * Title: Comparator used to sort court room basic values.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Initial naive implementation that sorts purely based on the crest court room
 * number. Implemented as a 'factory' style singleton so that we can easily
 * change implementation in the future.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: CourtRoomBasicValueComparator.java,v 1.2 2004/06/21 17:22:21
 *          sz0t7n Exp $
 */

public class CourtRoomBasicValueComparator implements Comparator {

    private static final CourtRoomBasicValueComparator _instance = new CourtRoomBasicValueComparator();

    private CourtRoomBasicValueComparator() {
    }

    /**
     * Get a comparator to sort court rooms.
     * 
     * @return A class implementing Comparator able to sort by
     *         <code>XhbCourtRoomBasicValue</code>.
     */
    public static final Comparator getInstance() {
        return _instance;
    }

    public int compare(Object o1, Object o2) {
        XhbCourtRoomBasicValue sort1 = (XhbCourtRoomBasicValue) o1;
        XhbCourtRoomBasicValue sort2 = (XhbCourtRoomBasicValue) o2;
        int sortReturn = 0;
        if (sort1.getCourtSiteCode() != null) {
            sortReturn = sort1.getCourtSiteCode().compareTo(sort2.getCourtSiteCode());
        }
        if (sortReturn == 0) {
            sortReturn = sort1.getCrestCourtRoomNo().compareTo(sort2.getCrestCourtRoomNo());
        }
        return sortReturn;
    }

    public boolean equals(Object obj) {
        return this == obj;
    }
}