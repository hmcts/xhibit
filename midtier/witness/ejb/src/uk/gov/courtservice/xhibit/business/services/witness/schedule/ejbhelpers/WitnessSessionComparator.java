package uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;

/*
 * Util Class to order Witness Sessions by Trial Day.
 */

public class WitnessSessionComparator implements java.util.Comparator {
    private static final WitnessSessionComparator instance = new WitnessSessionComparator();

    public static WitnessSessionComparator getInstance() {
        return instance;
    }

    private WitnessSessionComparator() {

    }

    public int compare(Object o, Object ob) {
        return compare((WitnessSession) o, (WitnessSession) ob);
    }

    public int compare(WitnessSession sd1, WitnessSession sd2) {

        return sd1.compareTo(sd2);
    }
}
