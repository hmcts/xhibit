package uk.gov.courtservice.xhibit.client.originalcharges.maintainoriginalcharge;

import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

public class MaintainOriginalChargeDialog extends XDialog {

    private String resources = XhibitBundles.OriginalCharges;

    public MaintainOriginalChargeDialog(Frame frame, MaintainOriginalChargeModel model)
    throws CSRecoverableException 
    {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(
            resources,
           (model.getMode().toLowerCase() + "TitleBarLbl" ))
        );
        super.addBodyPanel(new MaintainOriginalChargePanel(this, model));
        super.pack();
        super.setResizable(false);
    }
}
