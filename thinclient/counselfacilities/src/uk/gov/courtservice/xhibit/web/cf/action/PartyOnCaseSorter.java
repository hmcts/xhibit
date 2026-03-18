package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

/**
 * <p>
 * Title: PartyOnCaseSorter
 * </p>
 * <p>
 * Description: Utiltity class for managing which rows and in what order they
 * should be displayed in the Cousel Facility sign in and related pages.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */
public class PartyOnCaseSorter implements Comparator {
    public static final int COURTNAME = 1;

    public static final int DEFENDANT = 4;

    private int sortByName;

    private boolean ascending = true;

    private static final Integer UNASSIGNED = new Integer(9999);

    /**
     * Use the constructor to define how sorting is to be done.
     * 
     * @param sortByName
     *            use static values COURTNAME or DEFENDANT
     * @param isAscending
     */
    public PartyOnCaseSorter(int sortByName) {
        this.sortByName = sortByName;
    }

    /**
     * Compares two PartyOnCaseValue objects
     * 
     * @param o1
     * @param o2
     * @return
     * @throws ClassCastException
     *             if either o1 or o2 is not an instance of PartyOnCaseValue
     */
    public int compare(Object o1, Object o2) throws ClassCastException {
        PartyOnCaseValue p1 = (PartyOnCaseValue) o1;
        PartyOnCaseValue p2 = (PartyOnCaseValue) o2;

        int returnVal = 0;

        // sort by the first name in the collection
        if (sortByName == DEFENDANT) {
            String caseType1 = p1.getCaseType();
            String caseType2 = p2.getCaseType();
            String p1Name = "";
            String p2Name = "";

            if (caseType1 != null && (caseType1.equals("A") || caseType1.equals("S") || caseType1.equals("T"))) {
                p1Name = getFirstPartyName(p1.getDefendants());
            } else {
                p1Name = p1.getCaseTitle();
            }
            if (caseType2 != null && (caseType2.equals("A") || caseType2.equals("S") || caseType2.equals("T"))) {
                p2Name = getFirstPartyName(p2.getDefendants());
            } else {
                p2Name = p2.getCaseTitle();
            }

            returnVal = compareVals(p1Name, p2Name);
        }

        // if the names are equal, or not searching on defendant name, then
        // order by the rest of the fields
        if (returnVal == 0) {
            returnVal = compareVals(p1.getCourtSiteCode(), p2.getCourtSiteCode());

            if (returnVal == 0) {
                returnVal = compareVals(p1.getIsFloating(), p2.getIsFloating());

                if (returnVal == 0) {
                    returnVal = compareVals(p1.getCrestCourtRoomNumber(), p2.getCrestCourtRoomNumber());

                    if (returnVal == 0) {
                        returnVal = compareVals(p1.getSittingSequenceNo(), p2.getSittingSequenceNo());

                        if (returnVal == 0) {
                            returnVal = compareVals(p1.getTimeListed(), p2.getTimeListed());

                            if (returnVal == 0) {
                                returnVal = compareVals(p1.getShSequenceNo(), p2.getShSequenceNo());
                            }
                        }
                    }
                }
            }
        }

        return (ascending ? returnVal : (returnVal * -1));
    }

    // Taken from original code of DisplayCouselSign in
    // @author Frederik Vandendriessche
    public static Vector getCourtRoomsInCourt(Collection col) {
        String unassigned = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                "assignlegalrep.unassigned");
        Vector v = new Vector();
        Iterator i = col.iterator();
        while (i.hasNext()) {
            PartyOnCaseValue p = (PartyOnCaseValue) i.next();
            Vector courtRoom = new Vector();

            if (p.getIsFloating().equals("1")) {
                courtRoom.add(UNASSIGNED);
                courtRoom.add(unassigned);
                courtRoom.add(unassigned);
                courtRoom.add(UNASSIGNED);
            } else {
                courtRoom.add(p.getCourtRoomId());
                courtRoom.add(p.getCourtRoomName());
                courtRoom.add(p.getCourtRoomDisplayName());
                courtRoom.add(p.getCrestCourtRoomNumber());
            }
            v = addCourtRoom(courtRoom, v);
        }

        // Sort by crestCourtRoomNumber
        Collections.sort(v, new Comparator() {
            public int compare(Object o1, Object o2) {
                return compare((Vector) o1, (Vector) o2);
            }

            public int compare(Vector v1, Vector v2) {
                return compare((Integer) v1.get(3), (Integer) v2.get(3));
            }

            public int compare(Integer i1, Integer i2) {
                return i1.compareTo(i2);
            }
        });

        return v;
    }

    // Taken from original code of DisplayCouselSign in
    // @author Frederik Vandendriessche
    private static Vector addCourtRoom(Vector aRoom, Vector rooms) {
        Integer newRoomId = (Integer) aRoom.elementAt(0);
        boolean alreadyInThere = false;
        Iterator i = rooms.iterator();
        while (i.hasNext() && (!(alreadyInThere)))
            if (newRoomId.equals(((Vector) i.next()).elementAt(0)))
                alreadyInThere = true;
        if (!alreadyInThere)
            rooms.add(aRoom);
        return rooms;
    }

    /**
     * Filters a collection of partyOnCaseValues such that the returned
     * collection only contains those for the specified courtroomid
     * 
     * @param courtroomId
     * @param partyOnCaseValues
     * @return
     */
    public static Collection filterCollection(Integer courtroomId, Collection partyOnCaseValues) {
        Vector retCol = new Vector();

        if (courtroomId == null || courtroomId.intValue() == 0)
            return partyOnCaseValues;

        // seperate out unassiged cases

        int cid = courtroomId.intValue();
        Iterator it = partyOnCaseValues.iterator();

        while (it.hasNext()) {
            PartyOnCaseValue pocVal = (PartyOnCaseValue) it.next();
            Integer cfId = pocVal.getCourtRoomId();

            if (cfId != null && cfId.intValue() == cid) {
                if (pocVal.getIsFloating().equals("0")) {
                    retCol.add(pocVal);
                }

            }
            // unassigned cases requested: ignore pocVal.getCourtRoomId() as
            // this is irrelevant for floaters
            else if (cid == UNASSIGNED.intValue() && pocVal.getIsFloating().equals("1")) {
                retCol.add(pocVal);
            }
        }

        return retCol;
    }

    private static String getFirstPartyName(Collection col) {
        if (col != null) {
            final Iterator it = col.iterator();

            while (it.hasNext()) {
                DefendantValue defVal = (DefendantValue) it.next();

                if (defVal != null) {
                    return getDefendantName(defVal);
                }
            }
        }

        return "";
    }

    /**
     * Private method used to allow comparing of null value in a consistent
     * manner. If both val1 and val2 are null, then they are considered equal.
     * If val1 is null, and val2 is not, then val2 is greater If val2 is null,
     * and val1 is not, then val2 is less Otherwise, val1 is compared to val2
     * using the standard Comparable method val.compareTo(val2)
     * 
     * @param val1
     * @param val2
     * @return
     */
    private int compareVals(Comparable val1, Comparable val2) {
        int returnVal;

        if ((val1 == null) && (val2 == null))
            returnVal = 0;
        else if (val1 == null)
            returnVal = -1;
        else if (val2 == null)
            returnVal = 1;
        else
            returnVal = val1.compareTo(val2);

        return returnVal;
    }

    private static String getDefendantName(DefendantValue defVal) {
        String defName = CounselSignInRowGenerator.formatDefendantName(defVal);
        int id = defName.indexOf("<br>");
        return defName.substring(0, id);
    }
}
