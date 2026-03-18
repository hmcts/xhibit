package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.updatecase.UpdateDefendantModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
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
 * Company:
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class ChangeDefendantAction extends XAction {
    public ChangeDefendantAction() {
        populateFromBundle("ChangeDefendant");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        ChargesController chargesController;
        XhibitApplicationController xac;
        ChargesControllerModel ccm = null;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        Integer defendantID = ccm.getDefendantValue().getDefendantID();
        ApplicationCaseModel acm = ccm.getACM();
        Integer caseID = acm.getCaseId();

        UpdateDefendantModel uda = new UpdateDefendantModel(defendantID, caseID, ccm.isUserInCourtRoom());
        uda.setDefendantValue(ccm.getDefendantValue());

        XAction xa = XhibitActions.getAction(xac, XhibitActions.OpenAmendDefendant);
        xa.setModel(uda);
        xa.actionPerformed(e);

        // RAXs call to cal Fred's action. Is this all required?
        // Integer defendantID = ccm.getDefendantValue().getDefendantID();
        // XAction xa = XhibitActions.getAction(xac,
        // XhibitActions.UpdateDefendant);
        // xa.setModel(defendantID);
        // xa.actionPerformed(e);

        // **@todo ***** NOT SURE IF BELOW IS DONE IN FREDS SCREEN AND I JUST
        // PASS DEFENDANTID*****

        /** @todo get Defendant Details from defendantBD. */
        // defendantBD = new DefendantControllerBusinessDelegate();
        // defendantValue = defendantBD.getDefendantDetails(defendantID);
        /** @todo Display screen passing defendantValue object FRED */

        /** @todo Receive back an amended defendantValue object and update */
        // defendantBD.updateDefendant(defendantValue, caseStatusValue);
        // *******************************************
        // FRED: Need to call OpenAmendDefendantAction passing in defendantID
        // Refresh Charges tab
        chargesController.loadCharges();
    }
}