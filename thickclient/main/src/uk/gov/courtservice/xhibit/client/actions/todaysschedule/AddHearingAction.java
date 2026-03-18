package uk.gov.courtservice.xhibit.client.actions.todaysschedule;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.schedule.TodaysScheduleController;
import uk.gov.courtservice.xhibit.client.schedule.addhearing.AddHearingWizardDialog;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */

public class AddHearingAction extends XAction {

    public AddHearingAction() {
        populateFromBundle("AddHearing");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        xac = (XhibitApplicationController) getController();

        AddHearingWizardDialog addHearingWizardDialog = new AddHearingWizardDialog(xac);
        addHearingWizardDialog.setVisible(true);

        // Reload the today's schedule if the user didn't cancel.
        if (addHearingWizardDialog.getLatestEvent() == XWizardDialog.CANCEL_EVENT) {
            throw new UserCancelException();
        }
        ((TodaysScheduleController) xac.getBodyPanel()).reloadView();
    }
}