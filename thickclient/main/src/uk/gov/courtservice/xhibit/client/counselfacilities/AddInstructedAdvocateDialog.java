package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: AddInstructedAdvocateDialog
 * </p>
 * <p>
 * Description: The Dialog for adding an instructed advocate.  Usually these
 * are added on CREST and brought over on sync by Mercator.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class AddInstructedAdvocateDialog extends XDialog {
    
    private static final long serialVersionUID = 1L;

    private AddInstructedAdvocatePanel bodyPanel;

    private String resources = XhibitBundles.CounselFacilities;

    public AddInstructedAdvocateDialog(java.awt.Frame frame, AddInstructedAdvocateModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(resources, "lblAddInstructedAdvocate"));
        this.bodyPanel = new AddInstructedAdvocatePanel(this, model);
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
