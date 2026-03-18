package uk.gov.courtservice.xhibit.client.search.court;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;

/**
 * <p>
 * Title: XHIBIT 2
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
 * @author Bal Bhamra
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
        return "searchForCourts";

    }

    public Class[] getParameterTypes() {
        Class[] p = { String.class, String.class, Integer.class };
        return p;
    }

    public void setCriteria() {
        xs.debug("court setCriteria() start");
        addCriteria(String.class, "courtName");
        addCriteria(String.class, "courtType");
        addCriteria(Integer.class, "courtId", true);
        xs.debug("court setCriteria() end");
    }
}