package uk.gov.courtservice.xhibit.web.dr.action;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Default Action
 * </p>
 * <p>
 * Description: The default action for the application. <p/>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 */
public class ResetDataAction extends AbstractAction {

    private static final Logger log = CSServices.getLogger(ResetDataAction.class);

    /**
     * Empty default constructor
     */
    public ResetDataAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (actionEnvironment.getRequestParameterNames().hasMoreElements()) {
        	// Get elements from request based on what can be done here
            //String resetdob = (String) actionEnvironment.getRequestParameter("resetdob");
            //actionEnvironment.setRequestParameter("resetdob", resetdob);

        } else {
            actionEnvironment.setResponseName("reset_data");
        }
    }
}