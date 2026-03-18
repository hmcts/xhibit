package uk.gov.courtservice.xhibit.courtlog.cjse.eventmessage;

// third party
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;

/**
 * <p>
 * Title: CaseNumberMessageElement
 * </p>
 * <p>
 * Description: Retrives the case number message element
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: CaseNumberMessageElement.java,v 1.1 2004/04/20 14:40:56 pznwc5
 *          Exp $
 */

public class CaseNumberMessageElement implements MessageElement {
    private static final Logger log = CSServices.getLogger(CaseNumberMessageElement.class);

    public CaseNumberMessageElement() {
    }

    /**
     * Returns the case number message element comprised of xhb_case.case_type
     * and xhb_case.case_number
     * 
     * @param value
     *            not used
     * @param theCase
     *            Used to retrieve the case type and case number
     * @return
     */
    public String getElement(CourtLogSubscriptionValue value, XhbCase theCase) {
        log.debug("getElement() start: CourtLogSubscriptionValue " + value + "XhbCase " + theCase);

        // check parameters we're going to use
        if (theCase == null) {
            throw new IllegalArgumentException("An XhbCase reference must be " + "provided to the getElement() method");
        }

        log.debug("Returning CaseNumberMessageElement " + theCase.getCaseType() + theCase.getCaseNumber());

        return theCase.getCaseType() + theCase.getCaseNumber();
    }
}