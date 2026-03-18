package uk.gov.courtservice.xhibit.client.actions.counselfacilities;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindCounselDefendantDialog;
import uk.gov.courtservice.xhibit.client.counselfacilities.FindCounselDefendantModel;
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
public class FindCounselDefendantAction extends XAction {
    public FindCounselDefendantAction() {
        populateFromBundle("FindCounselDefendant");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        FindCounselDefendantModel model = new FindCounselDefendantModel();

        FindCounselDefendantDialog myDialog = new FindCounselDefendantDialog(
                (XhibitApplicationController) getController(), model);

        myDialog.setVisible(true);
    }
}
