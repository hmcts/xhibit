package uk.gov.courtservice.xhibit.client.actions.menu;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseProcess;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
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
 * @author unascribed
 * @version 1.0
 */

public class ViewChargesAction extends SynchXAction {
    private XhibitApplicationController xac;

    private ChargesController cc;

    public ViewChargesAction() {
        populateFromBundle("ViewCharges");
        setMnemonicKeyFromBundle("ViewCharges");
    }

    // public void xActionPerformed(ActionEvent e) throws
    // CSRecoverableException
    // {
    // if (getController()!=null) {
    // if (getModel()!=null) {
    // ChargesController cc = new
    // ChargesController((ApplicationCaseModel)getModel());
    // ((XhibitApplicationController)getController()).open(cc);
    // }
    // }
    // }

    public void preSynchActionPerformed(ActionEvent parm1) // throws
    // java.lang.Exception
    {
        xac = (XhibitApplicationController) getController();
    }

    public void synchActionPerformed(ActionEvent e) throws CSRecoverableException {
        if (xac != null && getModel() != null) {
            cc = new ChargesController((ApplicationCaseModel) getModel());
        }
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws CSRecoverableException {
        if (xac != null && cc != null) {
            xac.open(cc);
            
            //ctx-2487 disable appeal result if case create/amend
            if (xac.getCaseStatus().getCaseProcess() != CaseProcess.UPDATE) {
            	XhibitActions.getAction(xac, XhibitActions.AuthoriseResults).setEnabled(false);
            }
        }
    }

}