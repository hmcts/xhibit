package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesSelectionModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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

public class tbStayAction extends XAction {
    public static final int INDICTMENTS_TAB = 0;

    private ChargesController chargesController;

    private XhibitApplicationController xac;

    private ChargesControllerModel ccm = null;

    public tbStayAction() {
        populateFromBundle("ChargesTbStay");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        if (ccm.getSelectedChargeType() == INDICTMENTS_TAB) {
            processIndictment(e);
        } else {
            XHIBITConstant.debug("Charge is not an Indicment!  Charge type: " + ccm.getChargeValue().getChargeType());
            throw new CSRecoverableException("gui.user.StayError", "Charge is not an Indicment!  Charge type: "
                    + ccm.getChargeValue().getChargeType());
        }
    }

    private void processIndictment(ActionEvent e) throws CSRecoverableException {
        if (ccm.getSelectionType() == ChargesSelectionModel.DEFENDANT_SELECTION_TYPE) {
            // defendant column was selected but there is no defendant for
            // this count!
            // ... so Stay the count.
            if (ccm.getDefendantValue() == null) {
                XhibitActions.getAction(xac, XhibitActions.StayCount).actionPerformed(e);
            } else {
                XhibitActions.getAction(xac, XhibitActions.StayDefendantOnCount).actionPerformed(e);
            }
        } else if (ccm.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.StayCount).actionPerformed(e);
        } else {
            XhibitActions.getAction(xac, XhibitActions.StayIndictment).actionPerformed(e);
        }
    }

}