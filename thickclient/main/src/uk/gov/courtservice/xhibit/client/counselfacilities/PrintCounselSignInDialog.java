package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Frame;
import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: PrintCounselSignInDialog
 * </p>
 * <p>
 * Description: The dialog for printing the list of counsels signed in.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */

public class PrintCounselSignInDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private PrintCounselSignInPanel bodyPanel;

    private String resources = XhibitBundles.CounselFacilities;

    public PrintCounselSignInDialog(Frame frame, PrintCounselSignInModel model) throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(resources, "lblTitleBarPrintCounselSignIn"));
        this.bodyPanel = new PrintCounselSignInPanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.setResizable(false);
        super.pack();
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            this.bodyPanel.getFirstEnterableComponent().requestFocus();
        }
    }
}