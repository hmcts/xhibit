package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: EditPostDialog
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

public class EditPostDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private EditPostPanel bodyPanel;

    private String resources = XhibitBundles.CounselFacilities;

    public EditPostDialog(java.awt.Frame frame, EditPostModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        super.setTitle(XHIBITConstant.getResource(resources, "lblEditThePost") + " " + model.getPostNumber());
        this.bodyPanel = new EditPostPanel(this, model);
        super.addBodyPanel(bodyPanel);
        super.pack();
        super.setResizable(false);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        /*
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            this.bodyPanel.getFirstEnterableComponent().requestFocus();
        }
        */
    }
}

