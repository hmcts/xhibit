package uk.gov.courtservice.xhibit.client.search.offence;

import java.lang.reflect.Method;
import java.util.Vector;

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
        return null;
    }

    public Method getBusinessDelegateMethod() {
        Method m = null;
        return m;
    }

    public Class getValueObjectClass() {
        return uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue.class;
    }

    public void setValueObjectDisplay() {
        xs.debug("offence setValueObjectDisplay() start");
        addXHIBITValueObjectAttribute("statute");
        addXHIBITValueObjectAttribute("offenceType");
        addXHIBITValueObjectAttribute("actSection");
        xs.debug("offence setValueObjectDisplay() end");
    }

    // method helps determining the initial size of table columns
    public Vector getLongValues() {
        Vector v = new Vector();
        v.add("a long first name");
        v.add("1234");
        v.add("a very long last name");
        return v;
    }
}