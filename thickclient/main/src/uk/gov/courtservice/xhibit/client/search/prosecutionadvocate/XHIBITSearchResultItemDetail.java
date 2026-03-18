package uk.gov.courtservice.xhibit.client.search.prosecutionadvocate;

import java.lang.reflect.Method;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;
import uk.gov.courtservice.xhibit.courtlog.services.CourtLog2ControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

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
 * @version $Id: XHIBITSearchResultItemDetail.java,v 1.3 2005/01/24 10:46:08
 *          tz0d5m Exp $
 */
public class XHIBITSearchResultItemDetail extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchResultItemDetail {
    public XHIBITSearchResultItemDetail(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        // return DefendantControllerBusinessDelegate.class;
        return CourtLog2ControllerBeanBusinessDelegate.class;
    }

    // method to call to get one comeplete value object from the DB
    public Method getBusinessDelegateMethod() {
        // Return null for court log events, as the collection contains full,
        // complete vo's
        return null;
    }

    // Search implementors must implement this method
    public Class getValueObjectClass() {
        // return DefendantValue.class;
        return CourtLogViewValue.class;
    }

    public void setValueObjectDisplay() {
        XHIBITConstant.debug("prosecutionadvocate setValueObjectDisplay() start");
        // addXHIBITValueObjectAttribute("firstName",
        // XHIBITConstant.getResource(super.xs.rsc,"prosecutionadvocate.firstName"));
        // addXHIBITValueObjectAttribute("middleName",
        // XHIBITConstant.getResource(super.xs.rsc,"prosecutionadvocate.middleName"));
        // addXHIBITValueObjectAttribute("surName",
        // XHIBITConstant.getResource(super.xs.rsc,"prosecutionadvocate.lastName"));

        addXHIBITValueObjectAttribute("logEntry", XHIBITConstant.getResource(super.xs.rsc,
                "prosecutionadvocate.firstName"));

        XHIBITConstant.debug("prosecutionadvocate setValueObjectDisplay() end");
    }
}