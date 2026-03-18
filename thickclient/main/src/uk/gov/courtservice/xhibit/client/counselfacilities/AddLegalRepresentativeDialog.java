package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: AddLegalRepresentativeDialog
 * </p>
 * <p>
 * Description: The Dialog for adding a legal representative.
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

public class AddLegalRepresentativeDialog extends XDialog {
    
    private static final long serialVersionUID = 1L;

    private AddLegalRepresentativePanel bodyPanel;

    private String resources = XhibitBundles.CounselFacilities;

    public AddLegalRepresentativeDialog(java.awt.Frame frame, AddLegalRepresentativeModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(resources, "lblTitleBarAddLegalRep"));
        this.bodyPanel = new AddLegalRepresentativePanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(false);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            this.bodyPanel.getFirstEnterableComponent().requestFocus();
        }
    }
}