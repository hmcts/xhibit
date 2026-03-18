package uk.gov.courtservice.xhibit.client.actions.todaysschedule;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.schedule.othercase.OpenOtherCaseDialog;
import uk.gov.courtservice.xhibit.client.util.XAction;

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
 * @author unascribed
 * @version 1.0
 */

public class OpenOtherCaseAction extends XAction {

    public OpenOtherCaseAction() {
        populateFromBundle("OpenOtherCase");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        OpenOtherCaseDialog ood = new OpenOtherCaseDialog((java.awt.Frame) getController());
        ood.setVisible(true);
        // The following command is only required if you need to do more after
        // closing the window.
        // if (ood.isCancelClicked()) throw new UserCancelException();
    }
}