package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// java
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: CustodyLimitsDateMessageElement
 * </p>
 * <p>
 * Description: Retrieves the 'Custody Limits extended to ' date from the Xhibit
 * 'Bail and Custody' court log event (20200)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: CustodyLimitsDateMessageElement.java,v 1.1 2004/04/20 14:40:56
 *          pznwc5 Exp $
 */

public class CustodyLimitsDateMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(CustodyLimitsDateMessageElement.class);

    // The key to the custody date XPath in the properties file
    private static final String CUSTODY_DATE_XPATH_KEY = "custodyLimitsDateXpath";

    private Properties messageElementProperties;

    private String xpath;

    /**
     * load properties file and retrieve xpaths
     */
    public CustodyLimitsDateMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(CUSTODY_DATE_XPATH_KEY);
    }

    /**
     * Retrieves the custody limits extended to date from the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the date.
     * @param theCase
     *            not used
     * @return The custody limits date
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        return CSServices.getXMLServices()
                .getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(), xpath);
    }
}