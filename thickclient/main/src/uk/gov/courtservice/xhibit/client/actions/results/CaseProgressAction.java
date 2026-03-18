package uk.gov.courtservice.xhibit.client.actions.results;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.caseprogress.CaseProgressXPanel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: ViewCaseProgress
 * </p>
 * <p>
 * Description: The action that will launch the case progress screen.
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

public class CaseProgressAction extends SynchXAction {
    /**
     * The calling Xhibit instance.
     */
    private XhibitApplicationController xac;

    /**
     * The case progress screen.
     */
    private CaseProgressXPanel caseProgress;

    /**
     * Creates a CaseProgressAction
     */
    public CaseProgressAction() {
        populateFromBundle("ViewCaseProgress");
    }

    // public void xActionPerformed(ActionEvent e) throws
    // java.lang.Exception
    // {
    // XhibitApplicationController xac =
    // (XhibitApplicationController)getController();
    // xac.open(CaseProgressXPanel.class,
    // new Class[] {XhibitApplicationController.class},
    // new Object[] {xac});
    // }

    /**
     * Overrides SynchXAction method. Gets the calling Xhibit instance.
     * 
     * @param e
     *            ActionEvent
     */
    public void preSynchActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();

        // If the current XPanel has save functionality, this gives the
        // opportunity to prompt the user to save changes (if applicable).
        // This is to ensure any Pleas, Verdicts, Appeal Results and Disposals
        // have been saved.
        xac.callBodyPanelCloseLifeCycleMethods();
    }

    /**
     * SynchXAction implementation. Creates the case progress screen in a
     * separate thread.
     * 
     * @param e
     *            ActionEvent
     * @throws CSRecoverableException
     */
    public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (xac != null) {
            caseProgress = new CaseProgressXPanel(xac);
        }
    }

    /**
     * Overrides SynchXAction method. Displays the case progress screen.
     * 
     * @param e
     *            ActionEvent
     * @throws CSRecoverableException
     */
    public void postSynchActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (xac != null && caseProgress != null) {
            xac.open(caseProgress);
        }
    }
}
