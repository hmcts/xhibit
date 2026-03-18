package uk.gov.courtservice.xhibit.courtlog.helpers.event;

import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: EventLevelHelper
 * </p>
 * <p>
 * Description: Stores a map of Xhibit event Id to event level read in via the
 * EventLevelFactory. Provides a utility method to add the required ids to a
 * CourtLogCRUDValue give the event type.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: EventLevelHelper.java,v 1.6 2006/06/05 12:28:54 bzjrnl Exp $
 */
public class EventLevelHelper {
    private static final Logger LOG = CSServices.getLogger(EventLevelHelper.class);

    private static Map eventLevels = null;

    private EventLevelHelper() {
        // prevent external instantiation...
    }

    private static Map getEventLevels() {
        if (eventLevels == null) {
            eventLevels = EventLevelFactory.getEventLevels();
        }

        return eventLevels;
    }

    /**
     * Adds the ids required by the CJSE Events to the CourtLogCRUDValue
     * 
     * @param value
     *            value to add the additional parameters to
     * @param defendantOnCaseId
     *            Will be populated if case level event
     * @param defendantOnOffenceId
     *            Will be populated if crn level event
     */
    public static void addCjseCourtLogParameters(CourtLogCRUDValue value, Integer defendantOnCaseId,
            Integer defendantOnOffenceId) {
        LOG.debug("addCjseCourtLogParameters() start with CourtLogCRUDValue:" + value + " defendantOnCaseId: "
                + defendantOnCaseId + " defendantOnOffenceId: " + defendantOnOffenceId);
        if (value.getEventType() == null) {
            throw new IllegalArgumentException("Xhibit event type has not been set on the CourtLogCRUDValue");
        }
        // find the event level - the event levels are stored as Strings in the
        // Map
        String s = (String) getEventLevels().get(value.getEventType().toString());
        LOG.debug("Event Level = " + s);

        if (s == null) {
            /**
             * @todo: Reinstate when all events are in the spreadsheet. For now
             *        don't throw an Exception or the court log events will
             *        break.
             */
            // throw new CSUnrecoverableException(
            // "No event level found for event type: " +
            // value.getEventType());
            LOG.warn("No event level found for event type: " + value.getEventType());
        } else if (s.equals("CASE") || s.equals("JOINDER")) {
            // do nothing, we have all the info we need
        } else if (s.equals("DEFENDANT")) {
            // set defendantOnCaseId
            if (defendantOnCaseId == null) {
                // provides a double check that the earlier code supplied the
                // correct id required for this event level
                throw new IllegalArgumentException("A defendantOnCaseId must be provided for "
                        + "defendant level events");
            }
            value.setDefendantOnCaseId(defendantOnCaseId);
        } else if (s.equals("CRN")) {
            // set defendantOnOffenceId
            if (defendantOnOffenceId == null) {
                // provides a double check that the earlier code supplied the
                // correct id required for this event level
                throw new IllegalArgumentException("A defendantOnOffenceId must be provided for " + "crn level events");
            }
            value.setDefendantOnOffenceId(defendantOnOffenceId);
        } else {
            // some events may be case, defendant or crn level, to
            // be determined by the relevant event populator, we don't know
            // the
            // level at this point so can't provide a safety check, just set
            // the defendantOnCaseId and\or defendantOnOffenceId if set,
            // must
            // rely on the earlier code to get the event level correct
            value.setDefendantOnCaseId(defendantOnCaseId);
            value.setDefendantOnOffenceId(defendantOnOffenceId);
        }

        LOG.debug("addCjseCourtLogParameters() end with CourtLogCRUDValue:" + value + " defendantOnCaseId: "
                + defendantOnCaseId + " defendantOnOffenceId: " + defendantOnOffenceId);
    }
}
