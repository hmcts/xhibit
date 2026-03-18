package uk.gov.courtservice.xhibit.business.services.witness.schedule.ejbhelpers;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;

/*
 * Util Class to order Trial Sessions by Trial Day.
 */

public class TrialSessionComparator implements java.util.Comparator {
    private static final TrialSessionComparator instance = new TrialSessionComparator();

    public static TrialSessionComparator getInstance() {
        return instance;
    }

    private TrialSessionComparator() {

    }

    public int compare(Object o, Object ob) {
        return compare((TrialSession) o, (TrialSession) ob);
    }

    public int compare(TrialSession sd1, TrialSession sd2) {

        return sd1.compareTo(sd2);
    }
}
