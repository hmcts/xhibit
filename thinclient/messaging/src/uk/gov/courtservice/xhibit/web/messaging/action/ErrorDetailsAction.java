package uk.gov.courtservice.xhibit.web.messaging.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: ErrorDetailsAction
 * </p>
 * <p>
 * Description: The action for displaying error details
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.3 $ $Log:
 *         ErrorDetailsAction.java,v $ Revision 1.1 2003/04/03 08:44:08 rz3jq5
 *         Some changes.
 * 
 * Revision 1.1 2003/03/27 11:01:24 fz0n8j Added to cvs
 * 
 * 
 */
public class ErrorDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public ErrorDetailsAction() {
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
        actionEnvironment.setResponseName("errordetailscomplete");
    }

}
