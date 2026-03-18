package uk.gov.courtservice.xhibit.client.actions.todaysschedule;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class UpdateCaseAction extends XAction {

    public UpdateCaseAction() {
        populateFromBundle("UpdateCase");
        // setMnemonicKeyFromBundle("UpdateCase");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (getModel() != null) {
            if (!XhibitSingleton.getInstance().isUserInCourtroom()
                    || !(((ApplicationCaseModel) getModel()).getScheduledHearingValue().getCourtRoomId()
                            .equals(XhibitSingleton.getInstance().getCourtRoomId()))) {
                int rc = JOptionPane.showConfirmDialog((java.awt.Frame) getController(), XHIBITConstant.getResource(
                        XhibitBundles.TodaysSchedule, "OpenUpdateMessage"), XHIBITConstant.getResource(
                        XhibitBundles.TodaysSchedule, "OpenUpdateTitle"), JOptionPane.YES_NO_OPTION);
                if (rc != JOptionPane.YES_OPTION)
                    throw new UserCancelException();
            }

            // if
            // (myParent.getCurrentCourtRoom()==shv.getCourtRoomId().intValue()
            // && updateAction.isEnabled()) {

            ((XhibitApplicationController) getController()).openCase((ApplicationCaseModel) getModel(), true);
        }
    }
}
