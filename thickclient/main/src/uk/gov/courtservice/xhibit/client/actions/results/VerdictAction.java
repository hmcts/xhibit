package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.verdicts.VerdictsController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: VerdictAction
 * </p>
 * <p>
 * Description: action for verdicts
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Paul Morris
 * @version 1.0
 */

public class VerdictAction extends SynchXAction {
    private XhibitApplicationController xac;

    private VerdictsController vc;

    public VerdictAction() {
        populateFromBundle("Verdict");
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();

        // If the current XPanel has save functionality, this gives the
        // opportunity to prompt the user to save changes (if applicable).
        // This is to ensure any Pleas have been saved.
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        vc = new VerdictsController(xac.getApplicationCaseModel());
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (vc != null && xac != null) {
            xac.open(vc);
        }
    }
}
