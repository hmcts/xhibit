package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: JudgementOnDateMessageElement
 * </p>
 * <p>
 * Description: Retrieves the 'judgement on' date for Xhibit event 'Sentence'
 * (40750) sub event 'Bind over: to appear for judgement on [date]'.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: JudgementOnDateMessageElement.java,v 1.2 2005/02/11 10:06:57
 *          rzvddy Exp $
 */

public class JudgementOnDateMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(JudgementOnDateMessageElement.class);

    private static final String JUDGMENT_ON_DATE_XPATH_KEY = "judgmentOnDate";

    private final Properties messageElementProperties;

    private final String xpath;

    /**
     * Creates a JudgementOnDateMessageElement.
     */
    public JudgementOnDateMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(JUDGMENT_ON_DATE_XPATH_KEY);
    }

    /**
     * Retrieves the 'judgement on' date from the court log event
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the date.
     * @param theCase
     *            not used
     * @return String representing the 'judgement on' date.
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        return CSServices.getXMLServices()
                .getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(), xpath);
    }
}