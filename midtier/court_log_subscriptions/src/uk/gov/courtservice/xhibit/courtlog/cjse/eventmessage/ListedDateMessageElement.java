package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// java
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: ListedDateMessageElement
 * </p>
 * <p>
 * Description: Retrieves the listed date from the Xhibit court log event
 * (40702)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ListedDateMessageElement.java,v 1.1 2004/04/20 14:40:57 pznwc5
 *          Exp $
 */

public class ListedDateMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(ShortAdjournTimeMessageElement.class);

    // The key to the listed date XPath from the Directions by Case event
    private static final String LISTED_DATE_XPATH_KEY = "listedDate";

    // The key to the listed date XPath from the Long Adjourn event
    private static final String LONG_ADJ_DATE_XPATH_KEY = "longAdjournDate";

    // The Directions by Case Xhibit event type
    private static final Integer DIRECTIONS_BY_CASE_LIST = new Integer(40713);

    private Properties messageElementProperties;

    private String _xpathForDirections;

    private String _xpathForLongAdjourn;

    /**
     * load properties file and retrieve xpath
     */
    public ListedDateMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        _xpathForDirections = messageElementProperties.getProperty(LISTED_DATE_XPATH_KEY);
        _xpathForLongAdjourn = messageElementProperties.getProperty(LONG_ADJ_DATE_XPATH_KEY);
    }

    /**
     * Retrieves the listed date from the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the date.
     * @param theCase
     *            not used
     * @return The listed date
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        // check which event we are retrieveing the date from
        if (value.getCourtLogViewValue().getEventType().equals(DIRECTIONS_BY_CASE_LIST)) {
            return CSServices.getXMLServices().getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(),
                    _xpathForDirections);
        } else {
            return CSServices.getXMLServices().getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(),
                    _xpathForLongAdjourn);
        }
    }
}