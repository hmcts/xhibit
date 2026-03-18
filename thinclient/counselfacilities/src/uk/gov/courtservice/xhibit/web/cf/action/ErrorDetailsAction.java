package uk.gov.courtservice.xhibit.web.cf.action;

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
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.4 $ $Log:
 *         ErrorDetailsAction.java,v $ Revision 1.2 2003/04/23 16:08:43 nz5zpz
 *         merged from devBranch-2b-030404
 * 
 * Revision 1.1.2.1 2003/04/11 12:56:39 fz0n8j Initial Version
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
