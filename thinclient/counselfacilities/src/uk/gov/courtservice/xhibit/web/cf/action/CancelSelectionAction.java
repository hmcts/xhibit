package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Vector;

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
 * Description: The default action for the application.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 */
public class CancelSelectionAction extends AbstractAction {

    /**
     * The log4j logger
     */

    private static final Logger log = CSServices.getLogger(CancelSelectionAction.class);

    /**
     * Empty default constructor
     */
    public CancelSelectionAction() {
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
        // session timeed out - redirected to identify yourself page
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("home");
        } else {
            // if( !checkToken() )
            // { // throw new DuplicateFormSubmissionException();
            // }
            actionEnvironment.setSessionParameter("sortColStg", "");
            actionEnvironment.setSessionParameter("onlyRoom", "");
            actionEnvironment.setSessionParameter("legalRepCollection", new Vector());
            actionEnvironment
                    .setSessionParameter(DisplayCounselSignInAction.Key_PartiesOnCase_Collection, new Vector());
            actionEnvironment.setResponseName("identifylegrep");
        }
    }
}
