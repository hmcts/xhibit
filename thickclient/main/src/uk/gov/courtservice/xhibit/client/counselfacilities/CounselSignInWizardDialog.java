package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.event.WindowEvent;
import java.util.ArrayList;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class CounselSignInWizardDialog extends XWizardDialog {

    private static final long serialVersionUID = 1L;

    private ArrayList bodyPanels = new ArrayList();

    private String resources = XhibitBundles.CounselFacilities;

    public CounselSignInWizardDialog(java.awt.Frame frame, String title, boolean modal) throws CSRecoverableException {
        super(frame, title, modal);
        super.setTitle(XHIBITConstant.getResource(resources, "lblTitleBar"));

        CounselSignInModel model = new CounselSignInModel();
        model.setXac((XhibitApplicationController) frame);
        CounselSignInPanel counselSignIn = new CounselSignInPanel(this, model);
        // AssignRepresentativesPanel assignReps = new
        // AssignRepresentativesPanel( this, model );

        bodyPanels.add(counselSignIn);
        // bodyPanels.add( assignReps );
        super.addBodyPanels(bodyPanels);

        super.pack();
        super.setResizable(true);
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_OPENED) {
            ((CounselSignInPanel) this.bodyPanels.get(0)).getFirstEnterableComponent().requestFocus();
        }
    }
}
