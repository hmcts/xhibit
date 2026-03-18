package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: PHHelper
 * </p>
 * <p>
 * Description: Orders and creates the collection of events generated from the
 * Preliminary Hearing screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class PHEventHelper {
    private ArrayList eventOrder = new ArrayList();

    /**
     * Sets up the order of the events
     */
    public PHEventHelper() {
        // Set up the correct ordering, add all the event types to
        // the list in the order they should appear in the court log
        eventOrder.add(PHConstants.CASE_CALLED_ON);
        eventOrder.add(PHConstants.INDICTMENT_BY);
        eventOrder.add(PDHConstants.DEF_IDENTIFICATION);
        eventOrder.add(PDHConstants.DEF_BAIL);
        eventOrder.add(PDHConstants.DEF_FORMB);
        eventOrder.add(new Integer(30200)); // Long Adjournment
        eventOrder.add(new Integer(21300)); // Freetext
    }

    public void orderCourtLogEvents(ArrayList courtLogEvents) {
        // Order the events
        Collections.sort(courtLogEvents, EVENT_ORDER);
    }

    private final Comparator EVENT_ORDER = new Comparator() {
        public int compare(Object o1, Object o2) {
            CourtLogCRUDValue event1 = (CourtLogCRUDValue) o1;
            CourtLogCRUDValue event2 = (CourtLogCRUDValue) o2;
            int order1 = eventOrder.indexOf(event1.getEventType());
            int order2 = eventOrder.indexOf(event2.getEventType());

            return (order1 < order2 ? -1 : (order1 > order2 ? 1 : 0));
        }
    };
}
