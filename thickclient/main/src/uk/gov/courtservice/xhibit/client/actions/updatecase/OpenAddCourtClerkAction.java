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

public class OpenAddCourtClerkAction extends XAction {
    public OpenAddCourtClerkAction() {
        populateFromBundle("OpenAddCourtClerkAction");
    }

    public void xActionPerformed(ActionEvent e) {
        XHIBITConstant.debug("OpenAddCourtClerkAction: opening add court clerk dialog.");
        if (this.getCaller() != null) {
            Class callerClass = this.getCaller().getClass();
            if (callerClass
                    .isAssignableFrom(uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData.class)) {
                UpdateCourtStaffCaseData ucsd = (UpdateCourtStaffCaseData) this.getCaller();
                XHIBITConstant.debug("OpenAddCourtClerkAction: caller is UpdateCourtStaffCaseData");
                AddCourtClerkUsherDialog d = new AddCourtClerkUsherDialog((Frame) getController(), ucsd
                        .getUpdateCourtStaffModel(), this);
                ucsd.setModified();
                XHIBITConstant.debug("OpenAddCourtClerkAction: openend and closed add court clerk dialog.");
            }
        }
    }
}