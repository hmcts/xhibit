package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: View Witness By Case RefAction
 * </p>
 * <p>
 * Description: The action for searching for a witness.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * Author: David Duncan (2003) $$ $Log: ViewWitnessByCaseRefAction.java,v $
 * Author: David Duncan (2003) $$ Revision 1.5  2006/06/05 12:32:40  bzjrnl
 * Author: David Duncan (2003) $$ Change: TI901
 * Author: David Duncan (2003) $$ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Author: David Duncan (2003) $$
 * Author: David Duncan (2003) $$ Revision 1.4 2006/05/31 14:27:11 bzjrnl
 * Author: David Duncan (2003) $$ Change: TI901 Author: David Duncan (2003) $$
 * Comment: Weblogic Upgrade - Standadise code formatting Author: David Duncan
 * (2003) $$ Revision 1.3 2005/04/27 08:26:59 bzjrnl Manual Merge From
 * BRANCH_7_X
 * 
 * Revision 1.1.2.1 2005/04/25 13:37:46 bzjrnl Changes to move jspc into the
 * framework.
 * 
 * Revision 1.1 2003/04/02 15:41:13 hzf3bb new action
 * 
 * 
 */
public class ViewWitnessByCaseRefAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public ViewWitnessByCaseRefAction() {
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
        actionEnvironment.setResponseName("viewwitnessbycaserefcomplete");
    }
}
