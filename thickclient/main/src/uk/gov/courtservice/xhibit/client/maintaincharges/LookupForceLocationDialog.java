package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.event.WindowEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: LookupForceLocationDialog
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

public class LookupForceLocationDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    private LookupForceLocationPanel bodyPanel;
    

    public LookupForceLocationDialog(java.awt.Frame frame, LookupForceLocationModel model)
            throws CSRecoverableException {
        super(frame, "", true);
        String titleString = ResourceBundleHelper.getResource(
                XhibitBundles.AddCountsDefendantsResources, "forceLocationDialogTitle");
        super.setTitle(titleString);
        this.bodyPanel = new LookupForceLocationPanel(this, model);
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


