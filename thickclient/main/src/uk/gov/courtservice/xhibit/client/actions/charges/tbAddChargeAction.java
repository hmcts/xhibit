package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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

public class tbAddChargeAction extends XAction {
    public static final int INDICTMENTS_TAB = 0;

    public static final int BREACHES_TAB = 3;

    private ChargesController chargesController;

    private XhibitApplicationController xac;

    private ChargesControllerModel ccm = null;

    public tbAddChargeAction() {
        populateFromBundle("ChargesTbAddCharge");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        switch (ccm.getSelectedChargeType()) {
        case INDICTMENTS_TAB:
            processIndictment(e);
            break;
        case BREACHES_TAB:
            processBreach(e);
            break;
        default:
            throw new CSConfigurationException("Unknown tab: " + ccm.getSelectedChargeType());
        }
    }

    private void processIndictment(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddChargeAction: in processIndictment");
        XhibitActions.getAction(xac, XhibitActions.AddIndictment).actionPerformed(e);
    }

    private void processBreach(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddChargeAction: in processBreach");
        XhibitActions.getAction(xac, XhibitActions.AddBreach).actionPerformed(e);
    }
}