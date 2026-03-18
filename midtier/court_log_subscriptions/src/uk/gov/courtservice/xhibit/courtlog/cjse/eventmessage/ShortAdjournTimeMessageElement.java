package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// java
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: ShortAdjournTimeMessageElement
 * </p>
 * <p>
 * Description: Retrieves the short adjourn date from the Xhibit court log event
 * (30100)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ShortAdjournTimeMessageElement.java,v 1.1 2004/04/20 14:40:57
 *          pznwc5 Exp $
 */

public class ShortAdjournTimeMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(ShortAdjournTimeMessageElement.class);

    // The key to the short adjourn time XPath in the properties file
    private static final String SHORT_ADJ_TIME_XPATH_KEY = "shortAdjournTime";

    private Properties messageElementProperties;

    private String xpath;

    /**
     * load properties file and retrieve xpath
     */
    public ShortAdjournTimeMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(SHORT_ADJ_TIME_XPATH_KEY);
    }

    /**
     * Retrieves the short adjourn time from the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the time.
     * @param theCase
     *            not used
     * @return The short adjourn time
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        return CSServices.getXMLServices()
                .getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(), xpath);
    }
}