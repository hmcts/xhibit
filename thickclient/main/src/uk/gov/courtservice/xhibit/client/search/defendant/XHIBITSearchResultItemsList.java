package uk.gov.courtservice.xhibit.client.search.defendant;

import java.lang.reflect.Method;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

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

public class XHIBITSearchResultItemsList extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchResultItemsList {
    private static final Logger log = CSServices.getLogger(XHIBITSearchResultItemsList.class);

    public XHIBITSearchResultItemsList(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return DefendantControllerBeanBusinessDelegate.class;
    }

    // method to call to get one comeplete value object from the DB
    public Method getBusinessDelegateMethod() {
        Method m = null;

        Class[] parameterTypes = { Integer.class };
        try {
            DefendantControllerBeanBusinessDelegate.class.getMethod("getDefendantDetails", parameterTypes);
        } catch (Exception e) {
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);

        }
        return m;
    }

    // Search implementors must implement this method
    public Class getValueObjectClass() {
        return uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue.class;
    }

    public String getTitle() {
        return ResourceBundleHelper.getResource(xs.rsc, "defendant.step2.title");
    }

    public String getDescription() {
        return ResourceBundleHelper.getResource(xs.rsc, "defendant.step2.description");
    }

    public void setValueObjectDisplay() {
        log.debug("Defendant setValueObjectDisplay() start");
        addXHIBITValueObjectAttribute("firstName", ResourceBundleHelper
                .getResource(super.xs.rsc, "defendant.firstName"));
        addXHIBITValueObjectAttribute("middleName", ResourceBundleHelper.getResource(super.xs.rsc,
                "defendant.middleName"));
        addXHIBITValueObjectAttribute("surnNme", ResourceBundleHelper.getResource(super.xs.rsc, "defandant.lastName"));

        log.debug("Defendant setValueObjectDisplay() end");
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