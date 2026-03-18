package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.updatecase.AddCourtClerkUsherDialog;
import uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class OpenAddUsherAction extends XAction {

    public OpenAddUsherAction() {
        populateFromBundle("OpenAddUsherAction");
    }

    public void xActionPerformed(ActionEvent e) {
        XHIBITConstant.debug("OpenAddUsherAction: opening add usher dialog.");
        if (this.getCaller() != null) {
            Class callerClass = this.getCaller().getClass();
            if (callerClass
                    .isAssignableFrom(uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData.class)) {
                UpdateCourtStaffCaseData ucsd = (UpdateCourtStaffCaseData) this.getCaller();
                XHIBITConstant.debug("OpenAddUsherAction: caller is UpdateCourtStaffCaseData");
                AddCourtClerkUsherDialog d = new AddCourtClerkUsherDialog((Frame) getController(), ucsd
                        .getUpdateCourtStaffModel(), this);
                ucsd.setModified();
                XHIBITConstant.debug("OpenAddUsherAction: openend and closed add usher dialog.");
            }

        }
    }
}