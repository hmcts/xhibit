package uk.gov.courtservice.xhibit.web.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: ErrorSummaryAction
 * </p>
 * <p>
 * Description: The action for displaying error summary details
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 */
public class ErrorSummaryAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public ErrorSummaryAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) {
        actionEnvironment.setResponseName("errorsummarycomplete");
    }

}
