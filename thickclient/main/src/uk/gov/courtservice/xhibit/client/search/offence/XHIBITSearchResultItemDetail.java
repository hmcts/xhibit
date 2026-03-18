package uk.gov.courtservice.xhibit.client.search.offence;

import java.lang.reflect.Method;

import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITSearch;

/**
 * @deprecated
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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class XHIBITSearchResultItemDetail extends uk.gov.courtservice.xhibit.client.util.XHIBITSearchResultItemDetail {
    /**
     * @deprecated
     */
    public XHIBITSearchResultItemDetail(XHIBITSearch xs) {
        super(xs);
    }

    public Class getBusinessDelegate() {
        return ChargeControllerBeanBusinessDelegate.class;
    }

    public Method getBusinessDelegateMethod() {
        Method m = null;
        return m;
    }

    public Class getValueObjectClass() {
        return OffenceValue.class;
    }

    public void setValueObjectDisplay() {
        XHIBITConstant.debug("Defendant setValueObjectDisplay() start");
        addXHIBITValueObjectAttribute("statute");
        // addXHIBITValueObjectAttribute("offenceType"); // removed as by defect
        // id 161
        addXHIBITValueObjectAttribute("actSection");
        addXHIBITValueObjectAttribute("offenceDesc"); // defect id 238
        XHIBITConstant.debug("Defendant setValueObjectDisplay() end");
    }
}