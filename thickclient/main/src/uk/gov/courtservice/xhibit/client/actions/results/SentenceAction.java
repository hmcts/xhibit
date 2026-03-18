package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseProcess;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: SentenceAction
 * </p>
 * <p>
 * Description: The sentence action
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

public class SentenceAction extends SynchXAction {
    private XhibitApplicationController xac;

    private DisposalController dc;

    public SentenceAction() {
        populateFromBundle("Sentence");
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();

        // If the current XPanel has save functionality, this gives the
        // opportunity to prompt the user to save changes (if applicable).
        // This is to ensure any Pleas and Verdicts have been saved.
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        dc = new DisposalController(xac.getApplicationCaseModel());
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (dc != null && xac != null) {
            xac.open(dc);
            if (xac.getCaseStatus().getCaseCreateInProgressFlag() || xac.isCaseChargesDisposalsOpened()) {
            	xac.enableCourtLogActions(false); // disable court log actions
                XhibitActions.getAction(xac, XhibitActions.ViewCharges).setEnabled(true);
            }
            //ctx-2487 disable appeal result if case create/amend
            if (xac.getCaseStatus().getCaseProcess() != CaseProcess.UPDATE) {
            	XhibitActions.getAction(xac, XhibitActions.AuthoriseResults).setEnabled(false);
            }
        }
    }
}