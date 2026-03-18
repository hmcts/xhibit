package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.results.appealresults.AppealResultsController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Open the appeal results screen using a asynch action/p>
 * <p>
 * Description: action for appeal result
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class AppealResultAction extends SynchXAction {
    private XhibitApplicationController xac;

    private AppealResultsController arc;

    public AppealResultAction() {
        populateFromBundle("AppealResult");
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws Exception {
        xac = (XhibitApplicationController) getController();

        // If the current XPanel has save functionality, this gives the
        // opportunity to prompt the user to save changes (if applicable).
        // This is to ensure any Disposals have been saved.
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    public void synchActionPerformed(ActionEvent parm1) throws Exception {
        arc = new AppealResultsController(xac.getApplicationCaseModel());
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws Exception {
        if (arc != null && xac != null) {
            xac.open(arc);
        }
    }
}