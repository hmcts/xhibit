package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Frame;
import java.util.ResourceBundle;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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
 * @author Simon Gilmore
 * @version 1.0
 */

public class PrintChargesDialog extends XDialog {
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.MaintainCharges);

    private PrintChargesPanel bodyPanel;

    private PrintChargesModel model;

    public PrintChargesDialog(Frame frame, PrintChargesModel model) throws CSRecoverableException {
        super(frame, "", true, XDialog.OKCANCEL, XDialog.DEFAULTOK);
        super.setTitle(XHIBITConstant.getResource(myResources, "PrintCharges.Title"));
        this.model = model;

        bodyPanel = new PrintChargesPanel(this.model);
        addBodyPanel(bodyPanel);
        super.setResizable(false);
        pack();
    }
}