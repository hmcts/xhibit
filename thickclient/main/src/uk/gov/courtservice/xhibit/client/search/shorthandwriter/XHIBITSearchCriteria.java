package uk.gov.courtservice.xhibit.client.search.shorthandwriter;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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
    Class[] parameterTypes = { String.class, String.class, String.class, String.class, String.class, Integer.class };

    public XHIBITSearchCriteria(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return BisRefControllerBeanBusinessDelegate.class;
    }

    public String getMethodName() {
        return "searchForCourtReporters";
    }

    public Class[] getParameterTypes() {
        Class[] p = { String.class, String.class, String.class, String.class, String.class, Integer.class };
        return p;
    }

    public void setCriteria() {
        xs.debug("Shorthand Writer setValueObjectCriteria() start");
        addCriteria(String.class, "firstName");
        addCriteria(String.class, "midleName", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.middleName"));
        addCriteria(String.class, "initials");
        addCriteria(String.class, "surname", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.lastName"));
        addCriteria(String.class, "courtReporterFirmName", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.firm"));
        addCriteria(Integer.class, "courtId", "", true);
        xs.debug("Shorthand Writer  setValueObjectCriteria() end");
    }

    public String getNoMatchesText() {
        return XHIBITConstant.getResource(xs.rsc, "shorthandwriter.nomatchestext");
    }
}