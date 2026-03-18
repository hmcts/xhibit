package uk.gov.courtservice.xhibit.client.actions.counselfacilities;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.counselfacilities.AssignRepresentativesDialog;
import uk.gov.courtservice.xhibit.client.counselfacilities.AssignRepresentativesModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
public class CounselSignInWizardAction extends XAction {
    AssignRepresentativesModel model;

    public CounselSignInWizardAction() {
        populateFromBundle("CounselSignInWizard");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        model = new AssignRepresentativesModel();
        model.setXac((XhibitApplicationController) getController());
        AssignRepresentativesDialog myDialog = new AssignRepresentativesDialog((Frame) getController(), model);

        myDialog.setVisible(true);

        if (myDialog.isCancelClicked()) {
            throw new UserCancelException();
        }
    }
}