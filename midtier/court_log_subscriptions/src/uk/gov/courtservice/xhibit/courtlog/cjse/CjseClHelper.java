package uk.gov.courtservice.xhibit.courtlog.cjse;

// jdk, j2ee
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourt;
import uk.gov.courtservice.xhibit.business.entities.xhb_subscr_event_control.XhbSubscrEventControlBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_subscr_event_control.XhbSubscrEventControlBeanHelper;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.EventLevelAndIdentifier;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

/**
 * <p>
 * Title: Helper class to CjseClListener
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Provides utility methods to support the business process in CjseClListener.
 * </p>
 * <p>
 * <B>NOTE:</B> There is a hack fix in the saveCJSEEvent() method addressing
 * the problem with event identifier identified in bug number X54459. This hack
 * fix must be removed as soon as the CJIT code/interface is fixed.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Eds
 * </p>
 * 
 * @author Bob Boothby
 * @version 1.0
 */

public class CjseClHelper {
    private static final Logger log = CSServices.getLogger(CjseClHelper.class);

    private Map _eventPopulators;

    private Map _eventCascades;

    /**
     * No arguments constructor used to create an instance of CjseClHelper.
     */
    public CjseClHelper() {
        // Get the populators.
        _eventPopulators = CjseEventPopulatorMapFactory.getEventPopulators();
        // Get the cascades.
        _eventCascades = CjseEventCascadeFilterFactory.getEventCascadeFilters();
    }

    /**
     * This method takes a CourtLogSubscriptionValue representing a Court Log
     * Event and uses it to produce a CJSE event.
     * 
     * @param obj
     *            the court log event.
     * @throws NumberFormatException
     *             when the event type id is not a number.
     */
    public void transformClEvent(CourtLogSubscriptionValue obj) throws NumberFormatException {
        String methodName = "transformClEvent: ";
        log.debug(methodName + "CLSubsValue: " + obj);

        // Available in Object
        Integer courtSiteId = obj.getCourtSiteId();

        // Get the case Entity
        XhbCase theCase = null;
        try {
            theCase = XhbCaseBeanHelper.findByPrimaryKey(obj.getCourtLogViewValue().getCaseId());
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            log.error("Could not get case information for court site id : " + courtSiteId);
            return;
        }

        // Get court information.
        XhbCourt theCourt = theCase.getXhbCourt();
        String crestCourtId = theCourt.getCrestCourtId();

        // Integer courtId = theCourt.getCourtId();

        // Get the relevant data from the value object and the database.
        CourtLogViewValue objViewValue = obj.getCourtLogViewValue();

        // check for event cascades
        Integer[] eventIds = (Integer[]) _eventCascades.get(objViewValue.getEventType());
        if (eventIds == null) {
            eventIds = new Integer[] { objViewValue.getEventType() };
        }

        log.debug(methodName + "CREST COURT ID" + crestCourtId + "; No. events = " + eventIds.length);

        // for each sub event, or for the single event if no cascade found...
        for (int i = 0; i < eventIds.length; i++) {
            log.debug(methodName + "eventIds[" + i + "] = " + eventIds[i]);
            // Find an event populator to create the cjse event
            CjseEventPopulator populator = (CjseEventPopulator) _eventPopulators.get(eventIds[i]);
            if (populator != null) {
                // use the event populator to generate the output XML and
                // persist the result.
                createCjseEvent(crestCourtId, obj, objViewValue, populator, theCase);
            } else {
             
                if (log.isDebugEnabled()) {
                    log.debug("There is no populator configured for the Xhibit event type: " + objViewValue.getEventType()
                            + " this event will not be sent " + "to the CJSE");
                }
                return;
            }
        }
        log.debug(methodName + "Finished");
    }

    /**
     * This method takes all relevant information, constructs and persists the
     * output CJSE event for Mercator to collect and deliver to the CJSE.
     * 
     * @param crestCourtId
     *            The court as identified in crest.
     * @param obj
     *            The CourtLogSubscriptionValue for an XHIBIT message.
     * @param objViewValue
     *            The CourtLogViewValue for an XHIBIT message.
     * @param populator
     *            The populator for the particular event type.
     * @param theCase
     *            The case entity for the case the event is created on
     * @throws NumberFormatException
     *             When the event type id is not a number.
     */
    public void createCjseEvent(String crestCourtId, CourtLogSubscriptionValue obj, CourtLogViewValue objViewValue,
            CjseEventPopulator populator, XhbCase theCase) throws NumberFormatException {
        log.debug("createCjseEvent - Start: CREST COURT ID" + crestCourtId);
        // Set up the event parameters for population.
        EventParameters eventParameters = new EventParameters();

        // Populate the event.
        EventLevelAndIdentifier levelAndIdentifier = populator.populateCjseEvent(obj, theCase, eventParameters,
                Locale.UK);
        log.debug("createCjseEvent - done populateCjseEvent");

        if (levelAndIdentifier == null) {
            // this event is not going to the CJSE
            log.debug("createCjseEvent - levelAndIdentifier == null; returning");
            return;
        }

        if (levelAndIdentifier.getEventLevel() == null) {
            // this event is not going to the CJSE
            log.debug("createCjseEvent - levelAndIdentifier.getEventLevel() == null; returning");
            return;
        }

        // Marshal the event back to XML
        Marshaller marshaller = null;
        StringWriter xmlOutput = new StringWriter();
        try {
            marshaller = new Marshaller(xmlOutput);
            marshaller.setNamespaceMapping("be", "urn:integration-cjsonline-gov-uk:pilot:entities");

            marshaller.marshal(eventParameters);
        } catch (ValidationException ex) {
            log.error("Invalid XML generated by populator.", ex);
            return;
        } catch (MarshalException ex) {
            log.error("Could no marshal XML generated by populator.", ex);
            return;
        } catch (IOException ex) {
            log.error("Insane exception due to a string not being successfully " + "wrapped in a StringWriter.", ex);
            return;
        }

        String logEntry = xmlOutput.toString();

        logEntry = stripXMLHeading(logEntry);

        Date eventTime = eventParameters.getEventTime();
        Integer eventType = Integer.valueOf(eventParameters.getEventTypeID());

        // save the event to the database.
        saveCJSEEvent(crestCourtId, eventType, logEntry, eventTime, levelAndIdentifier);

        log.debug("createCjseEvent - Finished");
    }

    /**
     * This method is used to persist the newly translated CJSE event for
     * Mercator to collect and deliver.
     * <p>
     * <B>NOTE:</B> There is a hack fix in this method addressing the problem
     * with event identifier identified in bug number X54459. This hack fix must
     * be removed as soon as the CJIT code/interface is fixed.
     * 
     * @param crestCourtId
     *            The crest court identifier.
     * @param eventType
     *            The CJSE event type.
     * @param logEntry
     *            The CJSE event parameter XML
     * @param eventTime
     *            The date/time of the event.
     * @param levelAndIdentifier
     *            The event level and identifier. The identifier will be the
     *            case number for case level events, ASN\null for defendant
     *            level events, CRN\null for CRN level events
     */
    public void saveCJSEEvent(String crestCourtId, Integer eventType, String logEntry, Date eventTime,
            EventLevelAndIdentifier levelAndIdentifier) {
        log.debug("saveCJSEEvent Start: CREST COURT ID" + crestCourtId);
        // Create a SubscrEventControlBasicValue object and populate it.
        XhbSubscrEventControlBasicValue subEvtControlBasicValue = new XhbSubscrEventControlBasicValue();
        subEvtControlBasicValue.setEventType(eventType);
        subEvtControlBasicValue.setEventData(logEntry);
        subEvtControlBasicValue.setEventLevel(levelAndIdentifier.getEventLevel());
        subEvtControlBasicValue.setEventIdentifier(levelAndIdentifier.getEventIdentifier());

        /** todo need to check whether java.sql.Date appropriate */
        subEvtControlBasicValue.setEventTime(eventTime);
        subEvtControlBasicValue.setCrestCourtId(crestCourtId);

        log.debug("saveCJSEEvent - About to create XhbSubscrEventControl");

        // Store the new data in the table XHB_SUBSCR_EVENT_CONTROL
        XhbSubscrEventControlBeanHelper.create(subEvtControlBasicValue);

        log.debug("saveCJSEEvent - Finished");
    }

    private String stripXMLHeading(String logEntry) {
        int endTagPos = logEntry.indexOf("?>");
        return logEntry.substring(endTagPos + 2);
    }
}