package uk.gov.courtservice.xhibit.client.search.prosecutionadvocate;

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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class XHIBITSearchCriteria extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchCriteria {
    // public Collection searchForAdvocates(String firstName, String
    // surname, String initials, String middleName,
    // String chamberAddr, String advTypeInd, Integer crestAdvId, Integer
    // courtId)
    public XHIBITSearchCriteria(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return BisRefControllerBeanBusinessDelegate.class;
    }

    public String getMethodName() {
        return "searchForAdvocates";
    }

    public Class[] getParameterTypes() {
        Class[] p = { String.class, String.class, String.class, String.class, String.class, String.class,
                Integer.class, Integer.class };
        return p;
    }

    public void setCriteria() {
        xs.debug("prosecutionadvocate setCriteria() start");
        addCriteria(String.class, "firstName");
        addCriteria(String.class, "initials");
        addCriteria(String.class, "middleName");
        addCriteria(String.class, "surName");
        addCriteria(String.class, "chamberAddr");
        addCriteria(String.class, "advTypeInd", true); // set by xxxx to
        // 'Prosecution' or
        // something of the kind
        addCriteria(Integer.class, "crestAdvId");
        addCriteria(Integer.class, "courtId", true);
        xs.debug("prosecutionadvocate setCriteria() end");
    }
}