package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// java
import java.util.Properties;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: ReservedToJudgeMessageElement
 * </p>
 * <p>
 * Description: Finds the Judge Name for the Long Adjorn event 'Reserved to
 * [Judge Name]' subevent. Xhibit court log event 30200
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ReservedToJudgeMessageElement.java,v 1.1 2004/04/20 14:40:57
 *          pznwc5 Exp $
 */

public class ReservedToJudgeMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(ReservedToJudgeMessageElement.class);

    // The key to the judge's name XPath in the properties file
    private static final String JUDGE_NAME_XPATH_KEY = "judgeNameXpath";

    private Properties messageElementProperties;

    private String xpath;

    /**
     * load properties file and retrieve xpaths
     */
    public ReservedToJudgeMessageElement() {
        messageElementProperties = CSServices.getConfigServices().getProperties("cjse.event");
        xpath = messageElementProperties.getProperty(JUDGE_NAME_XPATH_KEY);
    }

    /**
     * Retrieves the name of the Judge for the 'Reserved To' Long Adjourn event.
     * 
     * @param value
     *            Contains a <code>CourtLogViewValue</code> which contains the
     *            XML from which to retrieve the Judge's name.
     * @param theCase
     *            not used
     * @return The Judge's Name
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        return CSServices.getXMLServices()
                .getXpathValueFromXmlString(value.getCourtLogViewValue().getLogEntry(), xpath);
    }
}