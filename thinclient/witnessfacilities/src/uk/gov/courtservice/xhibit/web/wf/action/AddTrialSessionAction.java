package uk.gov.courtservice.xhibit.web.wf.action;

import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;

/**
 * <p>
 * Title: Add Trial Session Action
 * </p>
 * <p>
 * Description: The action for adding a trial session.
 * 
 * Copyright: Copyright (c) 2003 Company: EDS
 * 
 * @author David Duncan (2003)
 * @version $Id: AddTrialSessionAction.java,v 1.5 2006/06/05 12:32:38 bzjrnl Exp $
 */
public class AddTrialSessionAction extends AbstractAction {
    /**
     * Empty default constructor
     */
    public AddTrialSessionAction() {
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
        String caseid = (String) actionEnvironment.getRequestParameter("caseid");

        String pageRequestFrom = (String) actionEnvironment.getRequestParameter("pagesource");
        actionEnvironment.setRequestParameter("pagesource", pageRequestFrom);
        actionEnvironment.setRequestParameter("caseid", caseid);
        actionEnvironment.setResponseName("addtrialsessioncomplete");
    }
}
