package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.search.shorthandwriter.XHIBITSearchShorthandWriter;
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

public class OpenChangeShorthandWriterAction extends XAction {
    public OpenChangeShorthandWriterAction() {
        populateFromBundle("OpenChangeShorthandWriterAction");
    }

    public void xActionPerformed(ActionEvent e) {
        XHIBITConstant.debug("OpenChangeShorthandWriterAction: opening add shorthand writer dialog.");

        XHIBITSearchShorthandWriter xsSHW = new XHIBITSearchShorthandWriter();

        XHIBITConstant.debug("OpenChangeShorthandWriterAction: openend and closed add shorthand writer dialog.");

        if (this.getCaller() != null) {
            XHIBITConstant.debug("OpenChangeShorthandWriterAction: caller object present");
            Class callerClass = this.getCaller().getClass();
            if (callerClass
                    .isAssignableFrom(uk.gov.courtservice.xhibit.client.updatecase.UpdateCourtStaffCaseData.class)) {
                XHIBITConstant.debug("OpenChangeShorthandWriterAction: caller is UpdateCourtStaffCaseData");
                // ((UpdateCourtStaffCaseData)this.getCaller()).processOpenChangeShorthandWriterAction(xsSHW.getRefCourtReporter());
            }
        }

    }
}