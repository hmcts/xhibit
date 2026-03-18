package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
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

public class tbAddAction extends XAction {
    
    public static final int INDICTMENTS_TAB = 0;
    public static final int SECTION41S_TAB = 1;
    public static final int COMMITTALS_TAB = 2;
    public static final int BREACHES_TAB = 3;
    public static final int FAIL2APPEAR_TAB = 4;

    private ChargesController chargesController;

    private XhibitApplicationController xac;

    private ChargesControllerModel ccm = null;

    public tbAddAction() {
        populateFromBundle("ChargesTbAdd");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        switch (ccm.getSelectedChargeType()) {
        case INDICTMENTS_TAB:
            processIndictment(e);
            break;
        case SECTION41S_TAB:
            processSection41(e);
            break;
        case COMMITTALS_TAB:
            processCommittal(e);
            break;
        case BREACHES_TAB:
            processBreach(e);
            break;
        case FAIL2APPEAR_TAB:
            processFail2Appear(e);
            break;
        default:
            throw new CSConfigurationException("Unknown tab: " + ccm.getSelectedChargeType());
            // throw new CSRecoverableException("Unknown tab: " +
            // ccm.getSelectedChargeType());
        }
    }

    private void processIndictment(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddAction: in processIndictment");
        if (ccm.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.AddDefendantsToCount).actionPerformed(e);
        } else if (ccm.getSelectionType() == ChargesSelectionModel.CHARGE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.AddCount).actionPerformed(e);
        } else {
            XhibitActions.getAction(xac, XhibitActions.AddIndictment).actionPerformed(e);
        }
    }

    private void processSection41(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddAction: in processSection41");
        if (ccm.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.AddDefendantsToOffence).actionPerformed(e);
        } else {
            XhibitActions.getAction(xac, XhibitActions.AddS41Offence).actionPerformed(e);
        }
    }

    private void processCommittal(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddAction: in processCommittal");
        if (ccm.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.AddDefendantsToOffence).actionPerformed(e);
        } else {
            XhibitActions.getAction(xac, XhibitActions.AddC4SOffence).actionPerformed(e);
        }
    }

    private void processBreach(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddAction: in processBreach");
        if (ccm.getSelectionType() == ChargesSelectionModel.OFFENCE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.AddDefendantsToOffence).actionPerformed(e);
        } else if (ccm.getSelectionType() == ChargesSelectionModel.CHARGE_SELECTION_TYPE) {
            XhibitActions.getAction(xac, XhibitActions.AddBreachOffence).actionPerformed(e);
        } else {
            XhibitActions.getAction(xac, XhibitActions.AddBreach).actionPerformed(e);
        }
    }
    
    private void processFail2Appear(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.error("tbAddAction: in processFail2Appear, should not get here.");
    }
    
    
}
// String chargeType = ccm.getChargeValue().getChargeType();
// if (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType()))
// {
// processIndictment(e);
// }
// else if (chargeType.equals(ChargeTypes.SECTION_41.getChargeType()))
// {
// processSection41(e);
// }
// else if
// (chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType()))
// {
// processCommittal(e);
// }
// else if (chargeType.equals(ChargeTypes.BREACH.getChargeType()))
// {
// processBreach(e);
// }
// else
// {
// XHIBITConstant.debug("Unknown charge type: " + chargeType );
// }
