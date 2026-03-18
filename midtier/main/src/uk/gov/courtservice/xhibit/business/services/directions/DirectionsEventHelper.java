package uk.gov.courtservice.xhibit.business.services.directions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLogWorkFlow;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: DirectionsEventHelper
 * </p>
 * <p>
 * Description: Orders and creates the collection of events generated from the
 * P&D screen. Also handles the update of these events
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DirectionsEventHelper.java,v 1.3 2006/06/05 12:29:34 bzjrnl Exp $
 */
public class DirectionsEventHelper {
    private static final Logger log = CSServices.getLogger(DirectionsEventHelper.class);

    private ArrayList eventOrder = new ArrayList();

    /**
     * Sets up the order of the events
     */
    public DirectionsEventHelper() {
        // set up the correct ordering, add all the directions event types
        // to the list in the order they should appear
        eventOrder.add(DirectionsHelper.IDENTIFIED);
        eventOrder.add(DirectionsHelper.ARRAIGNED);
        eventOrder.add(DirectionsHelper.P_AND_D_FORM);
        eventOrder.add(DirectionsHelper.TRIAL_TIME_EST);
        eventOrder.add(DirectionsHelper.DIRECTIONS);
        eventOrder.add(DirectionsHelper.BAIL_AND_CUSTODY);
        eventOrder.add(DirectionsHelper.CERT_OF_ATTEND);
        eventOrder.add(DirectionsHelper.FORM_B);
        eventOrder.add(new Integer(30200)); // Long Adjournment
        eventOrder.add(new Integer(21300)); // freetext
    }

    /**
     * If we are creating events from the p&d screen they first need ordering as
     * specified in X54905. This method also handles update, however events will
     * only be edited one at a time so the ordering will not have any effect in
     * this case.
     * 
     * @param courtLogEvents
     *            The events to order and create \ update
     * @throws CourtLogBusinessException
     */
    public void orderAndCreateUpdateCourtLogEvents(ArrayList courtLogEvents) throws CourtLogBusinessException {
        // order the events
        Collections.sort(courtLogEvents, EVENT_ORDER);

        for (int i = 0; i < courtLogEvents.size(); i++) {
            CourtLogCRUDValue crudValue = (CourtLogCRUDValue) courtLogEvents.get(i);
            if (crudValue.getId() == null || crudValue.getId().intValue() == 0) {
                // create entry
                log.debug("*** Creating new court log entry ***");
                CourtLogWorkFlow.newEntry(crudValue);
            } else {
                // update entry
                log.debug("*** Updating existing court log entry ***");
                CourtLogWorkFlow.updateEntry(crudValue);
            }
        }
    }

    /**
     * Comparator used to order directions court log events in the order
     * specified by Court Service (X54950)
     */
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