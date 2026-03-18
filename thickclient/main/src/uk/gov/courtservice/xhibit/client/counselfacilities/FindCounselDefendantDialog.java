package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: FindCounselDefendantDialog
 * </p>
 * <p>
 * Description: The dialog for finding a counsel or defendant.
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

public class FindCounselDefendantDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private FindCounselDefendantPanel bodyPanel;

    private String resources = XhibitBundles.CounselFacilities;

    public FindCounselDefendantDialog(java.awt.Frame frame, FindCounselDefendantModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(resources, "lblTitleBarFindCounselDefendant"));
        model.setXac((XhibitApplicationController) frame);
        this.bodyPanel = new FindCounselDefendantPanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(true);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            this.bodyPanel.getFirstEnterableComponent().requestFocus();
        }
    }
}
