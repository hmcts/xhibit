package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesSelectionModel;
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
 * @author Simon Gilmore
 * @version 1.0
 */

public class tbChangeAction extends XAction {
    private ChargesController chargesController;

    private XhibitApplicationController xac;

    private ChargesControllerModel ccm = null;

    public tbChangeAction() {
        populateFromBundle("ChargesTbChange");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue cv = ccm.getChargeValue();

        if (ccm.getSelectionType() == ChargesSelectionModel.DEFENDANT_SELECTION_TYPE) {
            // defendant column was selected but there is no defendant for
            // this offence!
            // ... so update the count or offence.
            if (ccm.getDefendantValue() == null) {
                if (cv.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
                    XhibitActions.getAction(xac, XhibitActions.ChangeCount).actionPerformed(e);
                } else {
                    XhibitActions.getAction(xac, XhibitActions.ChangeOffence).actionPerformed(e);
                }
            } else {
                XhibitActions.getAction(xac, XhibitActions.ChangeDefendant).actionPerformed(e);
            }
        } else if (ccm.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE) {
            if (cv.getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
                XhibitActions.getAction(xac, XhibitActions.ChangeCount).actionPerformed(e);
            } else {
                XhibitActions.getAction(xac, XhibitActions.ChangeOffence).actionPerformed(e);
            }
        }
    }
}