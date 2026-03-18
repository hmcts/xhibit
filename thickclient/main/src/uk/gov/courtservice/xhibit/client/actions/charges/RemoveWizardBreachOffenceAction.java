package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddedOffencesPanelModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargeWizardModel;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XPanel;

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
/*
 * Ref Date Author Description
 * 
 * 14-08-2003 AW Daley AddedOffencesPanel changed to XPanel
 */
public class RemoveWizardBreachOffenceAction extends XAction {

    private static final long serialVersionUID = 1L;

    XPanel addedOffencesPanel;

    AddedOffencesPanelModel model;

    public RemoveWizardBreachOffenceAction() {
        populateFromBundle("RemoveWizardBreachOffence");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        if (getModel() != null) {
            model = (AddedOffencesPanelModel) getModel();
            addedOffencesPanel = model.getAddedOffencesPanel();
            XhibitApplicationController xac = (XhibitApplicationController)getController();
            ChargesControllerModel ccm = ((ChargesController) xac.getBodyPanel()).getModel();
            ChargeWizardModel cwm = model.getChargeWizardModel();
            //Get reference to offenceValue before it is deleted
            OffenceValue offence = model.getOffenceList().get(model.getSelectedRowIndex());
            //Remove offence
            model.removeOffenceAtIndex(model.getSelectedRowIndex());
            //Get reference to list for this deft
            List seqNoList = ccm.getAllDefendantsOnCaseSeqNosSortedMap().get(cwm.getDefendantOnCaseID());                        
            if (seqNoList != null && cwm.getDefendantID() != null) {
                DefendantOnOffenceComplexValue doocv = offence.getDefendantOnOffence(cwm.getDefendantID());
                if (doocv != null) {
                    Integer seqNo = doocv.getSeqNo();
                    //Remove deleted sequence number from list
                    seqNoList.remove(seqNo); 
                }
            }
            addedOffencesPanel.stepActivate();
            addedOffencesPanel.stepUpdateViewState();
        }

    }

}