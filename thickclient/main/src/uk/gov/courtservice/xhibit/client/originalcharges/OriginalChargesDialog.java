package uk.gov.courtservice.xhibit.client.originalcharges;

import java.awt.Dialog;
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class OriginalChargesDialog extends XDialog {

    private String resources = XhibitBundles.OriginalCharges;
    private OriginalChargesPanel bodyPanel;
    private OriginalChargesModel model;

    public OriginalChargesDialog(Frame frame, OriginalChargesModel model)
    throws CSRecoverableException 
    {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(resources, "originalChargesTitleBarLbl"));
        this.model = model;
        bodyPanel = new OriginalChargesPanel(this, this.model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(true);
    }
}
