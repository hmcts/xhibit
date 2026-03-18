package uk.gov.courtservice.xhibit.client.actions.linkedhearings;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogController;
import uk.gov.courtservice.xhibit.client.linkedcases.LinkedCasesHelper;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Action to unlink current case from chain
 * </p>
 * <p>
 * Description: The action unlinks the case, closes the current window and
 * refreshes all open windows to reflect unlinking The linkedCaseHelper exposes
 * methods to assist
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

public class UnlinkCaseAction extends XAction {
    /**
     * ResourceBundle resources
     */
    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.TodaysSchedule);

    /**
     * XhibitApplicationController xac
     */
    private XhibitApplicationController xac;

    /**
     * Action created using bundle <init>
     */
    public UnlinkCaseAction() {
        populateFromBundle("UnlinkCase");
    }

    /**
     * xActionPerformed - Performs unlinking
     * 
     * @param e
     *            parameter for xActionPerformed
     * @throws CSRecoverableException -
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        // Message asking for confirmation for unlinking of case.
        int rc = JOptionPane.showConfirmDialog((java.awt.Frame) getController(), XHIBITConstant.getResource(resources,
                "messageQuestionUnLinkCase"), XHIBITConstant.getResource(resources, "messageTitleUnLinkCase"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (rc != JOptionPane.YES_OPTION) {
            throw new UserCancelException();
        }

        // Get xac and shv
        xac = (XhibitApplicationController) getController();

        // Instantiate helper
        LinkedCasesHelper linkedCasesHelper = new LinkedCasesHelper(xac);

        // create empty array(size 1) of ScheduledHearingValue to hold latest
        // ScheduledHearingValue
        ScheduledHearingValue[] shvs = new ScheduledHearingValue[1];

        // place ScheduledHearingId into Integer array(size 1)
        Integer[] shvIDs = new Integer[1];
        shvIDs[0] = xac.getApplicationCaseModel().getScheduledHearingId();

        // get latest shv which will be unlinked from chain
        shvs = linkedCasesHelper.getDelegate().getScheduledHearings(shvIDs);

        if (shvs != null || shvs.length > 0) {
            // ensure ApplicationCaseModel is set with latest data and
            // editable
            xac.getApplicationCaseModel().setScheduledHearingValue(shvs[0]);
            xac.getApplicationCaseModel().setInEditMode(true);

            try {
                // Unlink case may throw exception if case is not linkled
                linkedCasesHelper.unLinkCase(shvs[0].getScheduledHearingBasicValue());

                // Unlink status updated
                xac.getApplicationCaseModel().setIsLinked(false);
                XhibitActions.getAction(xac, XhibitActions.UnlinkCase).setEnabled(false);

                // Manage Open Xacs through Singleton
                Iterator it = XhibitSingleton.getXacGroup().iterator();
                XhibitApplicationController currentXac;

                // Refreshing of courtlog header, title and Unlink action for
                // each open xac to
                // reflect unlink of current case.
                while (it.hasNext()) {
                    currentXac = (XhibitApplicationController) it.next();
                    if (currentXac.getBodyPanel() != null
                            && !(currentXac.getBodyPanel() instanceof TodaysScheduleController)) {
                        if (currentXac.getBodyPanel() instanceof CourtLogController) {
                            // Refresh courtlogheader
                            ((CourtLogController) currentXac.getBodyPanel()).loadCourtLogHeaderTableModel();
                            ((CourtLogController) currentXac.getBodyPanel()).getCourtLogHeaderTable().repaint();

                            // Set title to linked status retrived from
                            // newly loaded court log header
                            boolean linked = ((CourtLogController) currentXac.getBodyPanel()).isLinked();
                            currentXac.getApplicationCaseModel().setIsLinked(linked);

                            // Set enable status of unlink action for
                            // current xac
                            XhibitActions.getAction(currentXac, XhibitActions.UnlinkCase).setEnabled(linked);
                        }
                        XhibitHelper.setTitle(currentXac, currentXac.getBodyPanel());
                    }
                }

                // Close window for unliked case
                xac.getParentController().closeXhibitApplication(xac);
            } catch (UserCancelException ex) {
                // This exception is thrown when trying to unlink a case which
                // is not linked.
                // throwing the exception prevents excecution of the remaining
                // code.
                XHIBITConstant.debug("UnlinkAction.xActionPerformed(): Nothing to Unlink.");

                // Disable Unlink action if nothing to unlink
                XhibitActions.getAction(xac, XhibitActions.UnlinkCase).setEnabled(false);
            }
        }
    }
}