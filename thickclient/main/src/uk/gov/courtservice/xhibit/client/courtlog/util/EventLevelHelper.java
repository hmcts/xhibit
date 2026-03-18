package uk.gov.courtservice.xhibit.client.courtlog.util;

import java.util.Map;

import uk.gov.courtservice.xhibit.courtlog.helpers.event.EventLevelFactory;

/**
 * <p>
 * Title: Event Level Helper
 * </p>
 * <p>
 * Description: This class is used to get the map of event levels and map them
 * to static variables so that the appropriate screen can be shown.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class EventLevelHelper {
    static Map eventLevels = null;

    public static final int STANDARD_EVENT_LEVEL = 5;

    /**
     * No level recorded or not required for id Value: 0
     */
    public static final int NA = 0;

    /**
     * Case level events Value:1
     */
    public static final int CASE = 1;

    /**
     * Defendant level events Value: 5
     */
    public static final int DEFENDANT = 5;

    public static final int DEFENDANT_SPECIAL = 6;

    /**
     * CRN level events Value: 8
     */
    public static final int CRN = 8;

    /**
     * Joinder event Value: 9
     */
    public static final int JOINDER = 9;

    private EventLevelHelper() {
    }

    static private Map getEventLevels() {
        if (eventLevels == null) {
            eventLevels = EventLevelFactory.getEventLevels();
        }
        return eventLevels;
    }

    static public int getEventLevel(String xhibitId) {
        String s = (String) getEventLevels().get(xhibitId);
        if (s == null) {
            // this event could be mapped to a cascade in the mapping file
            // e.g.
            // long adjourn 30200 appearing in mapping.xml as 302001-302004,
            // in
            // this case take the event level from the first cascade event
            if (xhibitId.length() == STANDARD_EVENT_LEVEL) // make sure we
            // only re-try
            // once
            {
                return getEventLevel(xhibitId + "1");
            } else {
                return NA;
            }
        } else if (s.equals("DEFENDANT"))
            return DEFENDANT;
        else if (s.equals("CASE"))
            return CASE;
        else if (s.equals("CRN"))
            return CRN;
        else if (s.equals("JOINDER"))
            return JOINDER;
        else
            return DEFENDANT_SPECIAL;
    }
}