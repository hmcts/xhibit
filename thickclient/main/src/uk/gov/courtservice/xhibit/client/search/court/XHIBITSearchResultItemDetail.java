package uk.gov.courtservice.xhibit.client.search.court;

import java.lang.reflect.Method;

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
 * @author Bal Bhamra
 * @version 1.0
 */

/* deprecated */
public class XHIBITSearchResultItemDetail extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchResultItemDetail {

    /* deprecated */
    public XHIBITSearchResultItemDetail(XHIBITSearch xs) {
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
            BisRefControllerBeanBusinessDelegate.class.getMethod("searchForCourts", parameterTypes);
        } catch (Exception e) {
        }
        return m;
    }

    // Search implementors must implement this method
    public Class getValueObjectClass() {
        return RefCourtBasicValue.class;
    }

    // Search implementors must implement this method
    public String getTitle() {
        return "The details of the selected court"; // get from properties
    }

    // Search implementors must implement this method
    public String getDescription() {
        return "Click back to select more or anothor court.  Click OK to use the currently selected court";
    }

    public void setValueObjectDisplay() {
        XHIBITConstant.debug("searchforCourt setValueObjectDisplay() start");
        addXHIBITValueObjectAttribute("courtFullName", XHIBITConstant.getResource(super.xs.rsc, "court.courtName"));
        addXHIBITValueObjectAttribute("courtType", XHIBITConstant.getResource(super.xs.rsc, "court.courtType"));
        XHIBITConstant.debug("court setValueObjectDisplay() end");
    }

}
