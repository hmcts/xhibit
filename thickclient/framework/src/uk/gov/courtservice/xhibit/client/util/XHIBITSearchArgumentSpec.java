package uk.gov.courtservice.xhibit.client.util;

import java.lang.reflect.Method;

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

public abstract class XHIBITSearchArgumentSpec {

    protected Class businessDelegate;

    protected String businessDelegateName;

    protected String businessDelegateShortName; // does not end with

    // 'BusinessDelegate'

    protected Method businessDelegateMethod;

    protected XHIBITSearch xs;

    public XHIBITSearchArgumentSpec(XHIBITSearch xs) {
        this.xs = xs;
    }

    // Search implementors must implement this method
    public abstract Class getBusinessDelegate();

    // Search implementors must implement this method
    public abstract Method getBusinessDelegateMethod();

    public String getTitle(String stepName) {
        String title = XHIBITConstant.getResource(xs.rsc, "xs.gen." + stepName + ".title");
        try {
            title = XHIBITConstant.getResource(xs.rsc, xs.getComponentResourceKey().concat("." + stepName + ".title"));
        } catch (Exception e) {
            XHIBITConstant.debug("XHIBITSearchCriteria could not find a customized title for the '"
                    + xs.getComponentResourceKey() + "' " + stepName + " card");
        }
        return title;
    }

    public String getDescription(String stepName) {
        String description = XHIBITConstant.getResource(xs.rsc, "xs.gen." + stepName + ".description");
        try {
            description = XHIBITConstant.getResource(xs.rsc, xs.getComponentResourceKey().concat(
                    "." + stepName + ".description"));
        } catch (Exception e) {
            XHIBITConstant.debug("XHIBITSearchCriteria could not find a customized description for the '"
                    + xs.getComponentResourceKey() + "' " + stepName + " card");
        }
        return description;
    }
}