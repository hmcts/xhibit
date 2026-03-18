package uk.gov.courtservice.xhibit.web.ps.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: AddRecipientDetailsAction
 * </p>
 * <p>
 * Description: The action for adding recipient details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.10 $ $Log:
 *         AddRecipientDetailsAction.java,v $ Revision 1.8 2003/08/11 08:22:10
 *         bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.7 2003/03/19 21:14:55 fz0n8j Modified for new mappings.
 * 
 * Revision 1.6 2003/03/17 11:32:10 fz0n8j Added revision cvs comments. ecawley
 * 
 * 
 */
public class AddRecipientDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public AddRecipientDetailsAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * Set up the ID for addrecipientdetails.jsp
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) {
        actionEnvironment.setResponseName("addrecipientdetailscomplete");
    }

}
