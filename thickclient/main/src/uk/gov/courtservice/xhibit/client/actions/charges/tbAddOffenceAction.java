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

public class tbAddOffenceAction extends XAction {

    public static final int INDICTMENTS_TAB = 0;
    public static final int SECTION41S_TAB = 1;
    public static final int COMMITTALS_TAB = 2;
    public static final int BREACHES_TAB = 3;
    public static final int FAIL2APPEAR_TAB = 4;
    public static final int APPEAL_TAB = 5;

    private ChargesController chargesController;

    private XhibitApplicationController xac;

    private ChargesControllerModel ccm = null;

    public tbAddOffenceAction() {
        populateFromBundle("ChargesTbAddOffence");
    }

    public void xActionPerformed(ActionEvent e) throws java.lang.Exception {
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
        case APPEAL_TAB:
        	processAppeal(e);
        	break;
            
        default:
            throw new CSConfigurationException("Unknown tab: " + ccm.getSelectedChargeType());
        }
    }

    private void processIndictment(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddOffenceAction: in processIndictment");
        XhibitActions.getAction(xac, XhibitActions.AddCount).actionPerformed(e);
    }

    private void processSection41(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddOffenceAction: in processSection41");
        XhibitActions.getAction(xac, XhibitActions.AddS41Offence).actionPerformed(e);
    }

    private void processCommittal(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddOffenceAction: in processCommittal");
        XhibitActions.getAction(xac, XhibitActions.AddC4SOffence).actionPerformed(e);
    }

    private void processBreach(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddOffenceAction: in processBreach");
        XhibitActions.getAction(xac, XhibitActions.AddBreachOffence).actionPerformed(e);
    }
    private void processFail2Appear(ActionEvent e) throws CSRecoverableException {
        XHIBITConstant.debug("tbAddOffenceAction: in processFail2Appear");
        XhibitActions.getAction(xac, XhibitActions.AddBailActOffence).actionPerformed(e);
    }
    
    private void processAppeal(ActionEvent e) throws CSRecoverableException {
    	XHIBITConstant.debug("tbAddOffenceAction: in processAppeal");
    	XhibitActions.getAction(xac, XhibitActions.AddAppealOffence).actionPerformed(e);
    }
}
