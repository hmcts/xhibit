package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;

/**
 * Used to guarantee that sessions are displayed in the correct order
 * (introduced to fix Bug X53677)
 * 
 * This class has a unit test - please update as required
 * 
 * @author Jon Powell (Electronic Data Systems)
 * @date 01-Sep-03
 * @see TestTrialSessionComparator
 */
public class TrialSessionComparator implements Comparator {

    public int compare(Object object1, Object object2) {
        if (!(object1 instanceof TrialSession) || !(object2 instanceof TrialSession)) {
            throw new IllegalArgumentException("objects must both be of type 'TrialSession'");
        }

        TrialSession s1 = (TrialSession) object1;
        TrialSession s2 = (TrialSession) object2;

        // first compare the days
        short s1_day = s1.getDayNumber();
        short s2_day = s2.getDayNumber();
        if (s1_day < s2_day)
            return -1;
        else if (s2_day < s1_day)
            return 1;

        // get to here if on the same day
        String s1_type = s1.getSessionType();
        String s2_type = s2.getSessionType();
        if (s1_type.equals("M") && s2_type.equals("A"))
            return -1;
        else if (s1_type.equals("A") && s2_type.equals("M"))
            return 1;

        // shouldn't ever get two identical sessions, but allow for anyway
        return 0;
    }

}
