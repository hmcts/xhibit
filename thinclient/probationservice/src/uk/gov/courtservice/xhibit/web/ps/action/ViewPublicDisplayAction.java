package uk.gov.courtservice.xhibit.web.ps.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: ViewPublicDisplayAction
 * </p>
 * <p>
 * Description: The action for viewing the public display
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003)
 * 
 * $Revision: 1.3 $ $Log: ViewPublicDisplayAction.java,v $
 * $Revision: 1.3 $ Revision 1.3  2006/06/05 12:32:27  bzjrnl
 * $Revision: 1.3 $ Change: TI901
 * $Revision: 1.3 $ Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * $Revision: 1.3 $ $Revision: 1.3 $
 * Revision 1.2 2006/05/31 14:26:55 bzjrnl $Revision: 1.3 $ Change: TI901
 * $Revision: 1.3 $ Comment: Weblogic Upgrade - Standadise code formatting
 * $Revision: 1.3 $ Revision 1.1 2003/03/24 08:23:26 fz0n8j Added view daily
 * list, and public display
 * 
 * 
 */
public class ViewPublicDisplayAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public ViewPublicDisplayAction() {
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
        actionEnvironment.setResponseName("viewpublicdisplay");
    }
}
