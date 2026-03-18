package uk.gov.courtservice.xhibit.client.search.court;

import java.lang.reflect.Method;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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

/* deprecated */
public class XHIBITSearchResultItemsList extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchResultItemsList {

    /* deprecated */
    public XHIBITSearchResultItemsList(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return BisRefControllerBeanBusinessDelegate.class;
    }

    public Method getBusinessDelegateMethod() {
        Method m = null;
        return m;
    }

    public Class getValueObjectClass() {
        return RefCourtBasicValue.class;
    }

    public void setValueObjectDisplay() {
        XHIBITConstant.debug("searchforCourt setValueObjectDisplay() start");
        addXHIBITValueObjectAttribute("courtFullName", XHIBITConstant.getResource(super.xs.rsc, "court.courtName"));
        addXHIBITValueObjectAttribute("courtType", XHIBITConstant.getResource(super.xs.rsc, "court.courtType"));
        XHIBITConstant.debug("court setValueObjectDisplay() end");
    }

    public Vector getLongValues() {
        Vector v = new Vector();
        return v;
    }
}