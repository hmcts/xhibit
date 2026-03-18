package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.OpenOtherDaysLogDialog;
import uk.gov.courtservice.xhibit.client.courtlog.OpenOtherDaysLogModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * @author Stephen Tully
 * @version 1.0
 */
public class OpenOtherDaysLogAction extends XAction {
    public OpenOtherDaysLogAction() {
        populateFromBundle("OpenOtherDaysLog");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        OpenOtherDaysLogModel model = new OpenOtherDaysLogModel();

        model.setXhibitApplicationController((XhibitApplicationController) getController());

        OpenOtherDaysLogDialog oodlDialog = new OpenOtherDaysLogDialog((Frame) getController(), model);

        oodlDialog.setVisible(true);
    }
}