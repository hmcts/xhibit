package uk.gov.courtservice.xhibit.client.skeletonschedule.util;

import java.util.Arrays;
import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;

/**
 * <p>
 * Title: WitnessSessionSorter
 * </p>
 * <p>
 * Description: Utility class for sorting the Witness Sessions returned from the
 * middle tier. Currently implemented to sort as follows: Trial Day, Trial
 * Session (DESC), Time
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class WitnessSessionSorter {

    /**
     * Sorts a WitnessSession[] to display in the Skeleton Schedule screen.
     * 
     * @param Array
     *            of WitnessSession objects
     * @return The sorted array
     */
    public static WitnessSession[] sort(WitnessSession[] witnessSession) {
        // set up comparator
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof WitnessSession) || !(o2 instanceof WitnessSession)) {
                    return 0;
                }
                return compare((WitnessSession) o1, (WitnessSession) o2);
            }

            /**
             * Compare method to compare two WitnessSession objects to display
             * in the Skeleton Schedule table. The sort is on Day Number (ASC),
             * Session Type (DESC) and Time (ASC)
             * 
             * @param n1
             *            The first WitnessSession to compare
             * @param n2
             *            The second WitnessSession to compare
             * @return O if same, -1 if n1 < n2, 1 if n1 > n2
             */
            public int compare(WitnessSession n1, WitnessSession n2) {
                // Compare the day numbers
                int dayResult = new Integer(n1.getDayNumber()).compareTo(new Integer(n2.getDayNumber()));
                // Compare the session types
                // Sort the session types in descending order - the options
                // are M = morning, A = afternoon
                int typeResult = -n1.getSessionType().compareTo(n2.getSessionType());
                // Compare the expected time
                int timeResult = n1.getExpected().compareTo(n2.getExpected());
                // If the two WitnessSessions have the different day numbers
                // just compare these. If the day numbers are the same, compare
                // the session type, if th esession types are the same compare
                // expected times.
                return (dayResult == 0) ? ((typeResult == 0) ? timeResult : typeResult) : dayResult;
            }
        };

        // sort the array
        Arrays.sort(witnessSession, comp);
        return witnessSession;
    }
}