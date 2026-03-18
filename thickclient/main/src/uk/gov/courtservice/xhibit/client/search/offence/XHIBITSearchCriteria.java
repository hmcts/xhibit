package uk.gov.courtservice.xhibit.client.search.offence;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;

/**
 * <p>
 * Title: XHIBIT 2 Search For Defendant
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

/* deprecated */
public class XHIBITSearchCriteria extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchCriteria {
    /* deprecated */
    public XHIBITSearchCriteria(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return BisRefControllerBeanBusinessDelegate.class;
    }

    public String getMethodName() {
        return "searchForOffences";
    }

    public Class[] getParameterTypes() {
        Class[] p = { String.class, String.class, String.class, Integer.class };
        return p;
    }

    public void setCriteria() {
        xs.debug("offence setValueObjectCriteria() start");
        addCriteria(String.class, "statute");
        addCriteria(String.class, "offenceType");// to be removed as by
        // Defect Id 161

        /** @todo: Defect 238/161 ADD THIS CRITERIUM */
        // addCriteria(String.class, "offenceDescription");
        addCriteria(String.class, "actSection");
        addCriteria(Integer.class, "courtId", true);
        xs.debug("offence setValueObjectCriteria() end");
    }
}