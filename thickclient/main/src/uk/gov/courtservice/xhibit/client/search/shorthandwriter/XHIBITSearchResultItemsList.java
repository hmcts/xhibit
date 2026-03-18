package uk.gov.courtservice.xhibit.client.search.shorthandwriter;

import java.lang.reflect.Method;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterValue;
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

public class XHIBITSearchResultItemsList extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchResultItemsList {
    private static final Logger log = CSServices.getLogger(XHIBITSearchResultItemsList.class);

    public XHIBITSearchResultItemsList(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return BisRefControllerBeanBusinessDelegate.class;
    }

    // method to call to get one comeplete value object from the DB
    public Method getBusinessDelegateMethod() {
        Method m = null;

        Class[] parameterTypes = { Integer.class };
        try {
            BisRefControllerBeanBusinessDelegate.class.getMethod("getDefendantDetails", parameterTypes);
        } catch (Exception e) {
            log.fatal(e, e);
            throw new CSUnrecoverableException(e);

        }
        return m;
    }

    // Search implementors must implement this method
    public Class getValueObjectClass() {
        return RefCourtReporterValue.class;
    }

    public void setValueObjectDisplay() {
        XHIBITConstant.debug("shorthand writer setValueObjectDisplay() start");
        addXHIBITValueObjectAttribute("initials", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.initials"));
        addXHIBITValueObjectAttribute("firstName", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.firstName"));
        addXHIBITValueObjectAttribute("midleName", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.middleName"));
        addXHIBITValueObjectAttribute("surname", XHIBITConstant.getResource(xs.rsc, "shorthandwriter.lastName"));

        XHIBITConstant.debug("shorthand writer setValueObjectDisplay() end");
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