package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import java.util.Properties;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: EventSpecificTextMessageElement
 * </p>
 * <p>
 * Description: Retrieves the event specific text from the Xhibit court log event
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 */

public class EventSpecificTextMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(EventSpecificTextMessageElement.class);

    private static final String EVENT_SPECIFIC_TEXT_XPATH_KEY = "eventSpecificText";

    private Properties messageElementProperties;

    private String xpath;

    /**
     * load properties file and retrieve xpath
     */
    public EventSpecificTextMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(EVENT_SPECIFIC_TEXT_XPATH_KEY);
    }

    /**
     * Retrieves the event specific text from the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the time.
     * @param theCase
     *            not used
     * @return The event specific text
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        return CSServices.getXMLServices()
                .getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(), xpath);
    }
}