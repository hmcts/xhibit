package uk.gov.courtservice.xhibit.client.search.defendant;

import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
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

public class XHIBITSearchCriteria extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchCriteria {
    public XHIBITSearchCriteria(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return DefendantControllerBeanBusinessDelegate.class;
    }

    public String getMethodName() {
        return "getDefendantDetails";
    }

    public Class[] getParameterTypes() {
        Class[] p = { String.class, String.class, String.class };
        return p;
    }

    public void setCriteria() {
        xs.debug("defendant setCriteria() start");
        super.addCriteria(String.class, "firstName");
        super.addCriteria(String.class, "middleName");
        super.addCriteria(String.class, "surName");
        xs.debug("defendant setCriteria() end");
    }
}