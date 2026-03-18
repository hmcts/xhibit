package uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel;

// jdk
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;

/**
 * <p>
 * Title: AppealWitnessReadCjseEventLevelPopulator
 * </p>
 * <p>
 * Description: Delegates to the DefendantCjseEventLevelPopulator or
 * CaseCjseEventLevelPopulator depending on the sub event type. This is a
 * 'special case' class where an xhibit event maps to many cjse events with
 * different level. Currently 3 of these special case populators have been
 * identified (TrialWitnesSworn and LongAdjourn as well as this) - if further
 * special case events are identified we should look at incorporating this
 * behaviour into the framework.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version $Id: AppealWitnessReadCjseEventLevelPopulator.java,v 1.0
 * @see DefendantCjseEventLevelPopulator
 * @see CaseCjseEventLevelPopulator
 */

public class AppealWitnessReadCjseEventLevelPopulator implements CjseEventLevelPopulator {
    private static final Logger log = CSServices.getLogger(AppealWitnessReadCjseEventLevelPopulator.class);

    // The key to the XPath in the properties file for the element which
    // determines
    // which event level to use
    private static final String AWR_LEVEL_DIFFERENTIATOR_XPATH_KEY = "awrLevelDifferentiator";

    private static final String DEFENDANT_LEVEL_VALUE = "awrDefendantLevel";

    private Properties messageElementProperties;

    private String xpath;

    /**
     * load properties file and retrieve xpath
     */
    public AppealWitnessReadCjseEventLevelPopulator() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(AWR_LEVEL_DIFFERENTIATOR_XPATH_KEY);
    }

    /**
     * Determines the event sub type from the court log event xml and delegates
     * to the relevant level populator.
     * 
     * @param eventParameters
     *            the EventParameters instance to populate, this parameter is
     *            also passed to the appropriate populator
     * @param theCase
     *            to be passed to the appropriate populator
     * @param eventInfo
     *            contains a CourtLogViewValue containing the xml which
     *            determines the populator level, this parameter is also passed
     *            to the appropriate populator
     * @return the Event Level and Identifier for this event
     */
    public EventLevelAndIdentifier populate(EventParameters eventParameters, XhbCase theCase,
            CourtLogSubscriptionValue eventInfo) {
        // log start of method and check params
        logStartAndCheckParams(eventParameters, theCase, eventInfo);

        // find the value from the court log event xml which will tell us which
        // event level to use
        String differentiatorValue = CSServices.getXMLServices().getXpathValueFromXmlString(
                eventInfo.getCourtLogViewValue().getLogEntry(), xpath);

        // find the level and populator
        CjseEventLevelPopulator populator;
        if (differentiatorValue.equals(messageElementProperties.getProperty(DEFENDANT_LEVEL_VALUE))) {
            // use a defendant level populator
            populator = CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
                    CjseEventLevelPopulatorFactory.DEFENDANT_EVENT_LEVEL);
        } else {
            // use a case level populator
            populator = CjseEventLevelPopulatorFactory.getInstance().getCjseEventLevelPopulator(
                    CjseEventLevelPopulatorFactory.CASE_EVENT_LEVEL);
        }

        // attempt population
        return populator.populate(eventParameters, theCase, eventInfo);
    }

    // ---------------------------- Private Methods
    // -----------------------------//
    private void logStartAndCheckParams(EventParameters eventParameters, XhbCase theCase,
            CourtLogSubscriptionValue eventInfo) throws IllegalArgumentException {
        log.debug("populate() start");

        // check parameters
        if (eventInfo == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance "
                    + "must be passed to the populate() method");
        }
        if (eventInfo.getCourtLogViewValue() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue must be " + "passed to the populate() method");
        }
        if (eventParameters == null) {
            throw new IllegalArgumentException("An EventParameters instance must be passed to the "
                    + "populate() method for court log event type: " + eventInfo.getCourtLogViewValue().getEventType());
        }
        if (theCase == null) {
            throw new IllegalArgumentException("An XhbCase instance must be passed to the "
                    + "populate() method for court log event type: " + eventInfo.getCourtLogViewValue().getEventType());
        }
        if (eventInfo.getCourtLogViewValue().getLogEntry() == null) {
            throw new IllegalArgumentException("A CourtLogSubscriptionValue instance containing a  "
                    + "CourtLogViewValue with a not null logEntry attribute must be "
                    + "passed to the populate() method");
        }
    }
}