package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerException;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: RemoveLegRepAction
 * </p>
 * <p>
 * Description: Removes Signed in Legal Representatives
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe 2 Jan 2004
 * @version 1.0
 */

public class RemoveLegRepAction extends TerminalCookieAction {
    private static final Logger log = CSServices.getLogger(RemoveLegRepAction.class);

    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("home");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            String button = (String) actionEnvironment.getRequestParameter("submitbutton");

            if (button.equals("ok")) {
                // collect up the items selected
                Collection repCol = (Collection) actionEnvironment.getSessionParameter("repCollection");
                Vector delRepCol = new Vector();
                Object[] repColArray = repCol.toArray();

                for (int i = 0; i < repCol.size(); i++) {
                    try {
                        String cbx = (String) actionEnvironment.getRequestParameter("CHB_" + (i + 1));

                        if (cbx.equals("on")) {
                            delRepCol.add(repColArray[i]);
                        }
                    } catch (ParameterNotFoundException pex) { // ignore
                        log.info(pex);
                    }
                }

                try {
                    CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                            .removeSignedInLegalReps(delRepCol);
                    CounselSignInHelper helper = new CounselSignInHelper();
                    helper.perform(actionEnvironment, getCourtId(actionEnvironment), getCourtSiteId(actionEnvironment),
                            null);
                } catch (CounselFacilitiesControllerException cfce) {
                    throw new FrameworkException(cfce);
                }
            }

            actionEnvironment.setSessionParameter("mode", "signin");
            actionEnvironment.setResponseName("displaycounselsignin");
        }
    }
}