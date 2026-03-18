package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// java
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: LongAdjournDateMessageElement
 * </p>
 * <p>
 * Description: Retrieves the long adjourn date from the Xhibir court log event
 * (30200)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: LongAdjournDateMessageElement.java,v 1.1 2004/04/20 14:40:57
 *          pznwc5 Exp $
 */

public class LongAdjournDateMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(ShortAdjournTimeMessageElement.class);

    // The key to the long adjourn date XPath in the properties file
    private static final String LONG_ADJ_DATE_XPATH_KEY = "longAdjournDate";

    private Properties messageElementProperties;

    private String xpath;

    /**
     * load properties file and retrieve xpath
     */
    public LongAdjournDateMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(LONG_ADJ_DATE_XPATH_KEY);
    }

    /**
     * Retrieves the long adjourn date from the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the date.
     * @param theCase
     *            not used
     *
     * @return The long adjourn date
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        return CSServices.getXMLServices()
                .getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(), xpath);
    }

    /**
     * Added 20081104 Luis Valenzuela as the CASE not used in the function call
     * Retrieves the long adjourn date from the court log event
     * 
     * @param logEntry
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the date.
     *
     * @return The long adjourn date
     */
    public String getElement(String logEntry) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + logEntry );

        return CSServices.getXMLServices().getXpathValueFromXmlString(logEntry, xpath);
    }


}