package uk.gov.courtservice.xhibit.client.actions.linkedhearings;

import java.awt.event.ActionEvent;
import java.util.Iterator;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.linkedcases.LinkedCasesDialog;
import uk.gov.courtservice.xhibit.client.linkedcases.LinkedCasesHelper;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Action to link current case to available cases
 * </p>
 * <p>
 * Description: The action launches a Dialog which allows cases to be linked The
 * action then opens windows for each linked case ensure duplicate windows
 * aren't open. The linkedCaseHelper exposes methods to assist
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class LinkCasesAction extends XAction {
    /**
     * Action created using bundle <init>
     */
    public LinkCasesAction() {
        populateFromBundle("LinkCases");
    }

    /**
     * xActionPerformed - Performs Linking
     * 
     * @param e
     *            parameter for xActionPerformed
     * @throws CSRecoverableException -
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        // XhibitApplicationController linkedXac;

        // Dialog allows cases to be selected for linking
        LinkedCasesDialog linkedCasesDialog = new LinkedCasesDialog(xac);
        linkedCasesDialog.setVisible(true);

        // If cancelled then abandon action
        if (linkedCasesDialog.isCancelClicked()) {
            throw new UserCancelException();
        }

        // Get linkedcases from dialog
        CaseSchedHearingValue[] linkedCases = linkedCasesDialog.getLinkedCases();

        // If there are no linked cases then abondon action
        if (linkedCases == null) {
            throw new UserCancelException();
        }

        // Set linked flag on leading case
        xac.getApplicationCaseModel().setIsLinked(true);
        XhibitActions.getAction(xac, XhibitActions.UnlinkCase).setEnabled(true);

        // Create instance of LinkedCasehelper
        LinkedCasesHelper linkedCasesHelper = new LinkedCasesHelper(xac);

        // Retrieve collection of SHVs for linked cases
        ScheduledHearingValue[] shvs = linkedCasesHelper.getScheduleHearingValues(linkedCases);

        // Open windows for all linked case
        linkedCasesHelper.openSHVWindows(shvs);

        // Refresh each open Xac with linked case information so true linked
        // chain
        // is reflected in courtlog header
        Iterator it = XhibitSingleton.getXacGroup().iterator();
        XhibitApplicationController currentXac;

        while (it.hasNext()) {
            currentXac = (XhibitApplicationController) it.next();
            if (currentXac.getBodyPanel() != null && !(currentXac.getBodyPanel() instanceof TodaysScheduleController)) {
                if (currentXac.getBodyPanel() instanceof CourtLogController) {
                    // Refresh CourtLogHeaderTable with newly linked chain
                    ((CourtLogController) currentXac.getBodyPanel()).loadCourtLogHeaderTableModel();
                    ((CourtLogController) currentXac.getBodyPanel()).getCourtLogHeaderTable().repaint();
                }

                // Set appropriate title for xac (Linked or not?)
                XhibitHelper.setTitle(currentXac, currentXac.getBodyPanel());

                // Enable correct actions (Linked or not?)
                currentXac.enableCaseActions(true);
            }
        }
    }
}