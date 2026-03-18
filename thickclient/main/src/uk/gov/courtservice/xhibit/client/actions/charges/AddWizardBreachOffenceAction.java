package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddBreachOffenceDefendantDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddOffenceDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddedOffencesPanelModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargeWizardModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.HOProcCodeHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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

public class AddWizardBreachOffenceAction extends XAction implements SearchProcessHandler {
 
    private static final long serialVersionUID = 1L;

    private AddedOffencesPanelModel model;

    private XhibitApplicationController xac;
    
    private XPanel addedOffencesPanel;

    private ChargeWizardModel chargeWizardModel;
   
    private AddBreachOffenceDefendantDetailsDialog addBreachOffenceDefendantDetailsDialog;
    
    private ChargesControllerModel ccm;
    
    public AddWizardBreachOffenceAction() {
        populateFromBundle("AddWizardBreachOffence");
    }

    /**
     * XAction implementation called when the action is fired.
     * 
     * @param e
     *            The ActionEvent
     * @throws CSRecoverableException
     * @throws Exception
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        model = (AddedOffencesPanelModel) getModel();
        addedOffencesPanel = model.getAddedOffencesPanel();
        chargeWizardModel = model.getChargeWizardModel();
        xac = (XhibitApplicationController) getController();
        ccm = ((ChargesController) xac.getBodyPanel()).getModel();
        AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                (XhibitApplicationController) getController(), XhibitActions.OpenSearchOffence);
        sa.setCaller(this);
        sa.xActionPerformed(e);
        
    }

    /**
     * SearchProcessHandler implementation called when the search is complete
     * and the user has selected a breach offence from the search.
     * 
     * @param searchAction
     *            The search action
     * @throws CSRecoverableException
     */
    public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
        final Collection col = searchAction.getResults();
        final Iterator it = col.iterator();
        // User can only select one offence, so collection should contain 1 item
        if (it.hasNext()) {
            final RefOffenceBasicValue refOffence = (RefOffenceBasicValue) it.next();
            final Integer courtID = XhibitSingleton.getInstance().getCourtId();
            final Integer caseID = chargeWizardModel.getCaseID();
            final OffenceValue offenceValue = new OffenceValue();
            offenceValue.setCaseID(caseID);
            offenceValue.setCourtID(courtID);
            offenceValue.setRefOffenceID(refOffence.getId());
            offenceValue.setOffenceCode(refOffence.getOffenceCode());
            offenceValue.setOffenceDescription(refOffence.getOffenceDesc());
            offenceValue.setCourtLogDate(Calendar.getInstance());

            if (chargeWizardModel.getChargeType().getChargeType().equals(ChargeTypes.INDICTMENT.getChargeType())) {
                //Launch Add Additional Offence details functionality for Indictment Wizard
                AddOffenceDetailsDialog addOffenceDetailsDialog = 
                    new AddOffenceDetailsDialog(AddOffenceDetailsDialog.TITLE.COUNT,offenceValue, xac, HOProcCodeHelper.TRIAL, ChargesControllerHelper.MODE.ADD);
                addOffenceDetailsDialog.setVisible(true);
                if( addOffenceDetailsDialog.isCancelClicked())
                        throw new UserCancelException();

                //Wizard will take updated offenceValue above and update database.
                
            }else{
                //Launch Add Additional Breach Offence Defendants details functionality for Breach Wizard
                addBreachOffenceDefendantDetailsDialog = 
                    new AddBreachOffenceDefendantDetailsDialog(xac, offenceValue, chargeWizardModel, false, 
                            ChargesControllerHelper.MODE.ADD, ccm);
                
                addBreachOffenceDefendantDetailsDialog.setVisible(true);
                if (addBreachOffenceDefendantDetailsDialog.isCancelClicked()) 
                    throw new UserCancelException();
                else{
                    //Need to add seq no to list
                    List seqNoList = ccm.getAllDefendantsOnCaseSeqNosSortedMap().get(chargeWizardModel.getDefendantOnCaseID());
                    Integer seqNo = offenceValue.getDefendantOnOffence(chargeWizardModel.getDefendantID()).getSeqNo();
                    seqNoList.add(seqNo);
                }
                
                
                //Wizard will take updated offenceValue above and update database.
            }

            // The charge id will not exist for Indictments or Breaches that
            // are
            // in the process of being created.
            if (chargeWizardModel.getChargeID() != null) {
                offenceValue.setChargeID(chargeWizardModel.getChargeID());
            }
            
            model.addOffence(offenceValue);
            chargeWizardModel.setAddedOffences(model.getOffenceList()); //TODO Bal new HERE - Remove?
            addedOffencesPanel.stepActivate();
            addedOffencesPanel.stepUpdateViewState();
        } else {
            // User clicked cancel or close on search dialog.
            throw new UserCancelException();
        }
    }

    public AddedOffencesPanelModel getAddedOffencesPanelModel() {
        return this.model;
    }
}
