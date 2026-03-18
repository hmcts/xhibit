package uk.gov.courtservice.xhibit.client.search;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.client.delegate.CSBusinessDelegate;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: XHIBIT2 Search
 * </p>
 * <p>
 * Description: XHIBITSearchStep provided the genericity between the steps of
 * searching
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version $Revision: 1.3 $
 */
public abstract class XHIBITSearchStep {
    public CSBusinessDelegate delegate = null;

    protected final Logger log = CSServices.getLogger(getClass());

    public XHIBITSearchStep() {
        super();
    }

    // Search implementors must implement this method
    public abstract String getMethodName();

    // Search implementors must implement this method
    public abstract Class[] getParameterTypes();

    // Search implementors only to override if other BD required.
    public CSBusinessDelegate getBusinessDelegate() {
        if (delegate == null) {
            delegate = XhibitDelegateHelper.getBizRefDelegate();
        }
        return delegate;
    }

    /**
     * This method must return the key for the title of the search step
     * {criteria,results,details} screen
     * 
     * @return
     */
    public abstract String getStepTitleResourceKey();

    public final String getStepTitle() {
        return XHIBITConstant.getResource(XhibitBundles.XhibitSearch, this.getStepTitleResourceKey());
    }

    /**
     * This method must return the key for the description of the search step
     * {criteria,results,details} screen *
     * 
     * @return
     * @return
     */
    public abstract String getStepDescriptionResourceKey();

    public final String getStepDescription() {
        return XHIBITConstant.getResource(XhibitBundles.XhibitSearch, this.getStepDescriptionResourceKey());
    }
}