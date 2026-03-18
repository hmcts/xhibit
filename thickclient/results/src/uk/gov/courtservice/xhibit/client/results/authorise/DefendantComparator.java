package uk.gov.courtservice.xhibit.client.results.authorise;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBasicValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationReturnValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: DefendantComparator.java,v 1.5 2009/03/04 12:18:15 hewittm Exp $
 */

class DefendantComparator implements Comparator<DefendantAuthorisationReturnValue> {
    private static final DefendantComparator _instance = new DefendantComparator();

    private DefendantComparator() {
        // Change permisions of default constructor
    }

    /**
     * Get a comparator to sort court rooms.
     * 
     * @return A class implementing Comparator able to sort by
     *         <code>XhbCourtRoomBasicValue</code>.
     */
    public static final Comparator<DefendantAuthorisationReturnValue> getInstance() {
        return _instance;
    }

    public int compare(DefendantAuthorisationReturnValue o1, DefendantAuthorisationReturnValue o2) {
        DefendantAuthorisationReturnValue sort1 = o1;
        DefendantAuthorisationReturnValue sort2 = o2;

        /** @todo replace call to build defendant name with framework method */
        return buildDefendantName(sort1.getDefendant()).compareTo(buildDefendantName(sort2.getDefendant()));
    }

    public boolean equals(Object obj) {
        return this == obj;
    }

    /**
     * Remove this method when framework method is available.
     * 
     * @param def
     * @return
     */
    String buildDefendantName(XhbDefendantBasicValue def) {
        return def.getSurname() + ", " + def.getFirstName();
    }
}