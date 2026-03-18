package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.event.WindowEvent;

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
 * @author unascribed
 * @version 1.0
 */

public class AssignRepresentativesDialog extends XDialog {
    
    private static final long serialVersionUID = 1L;

    private AssignRepresentativesPanel bodyPanel;

    private String resources = XhibitBundles.CounselFacilities;

    public AssignRepresentativesDialog(java.awt.Frame frame, AssignRepresentativesModel model)
            throws CSRecoverableException {
        super(frame, "", true, XDialog.APPLYOKCANCEL, XDialog.DEFAULTCANCEL);
        super.setTitle(XHIBITConstant.getResource(resources, "lblTitleBar"));
        this.bodyPanel = new AssignRepresentativesPanel(this, model);
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