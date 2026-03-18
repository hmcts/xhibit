package uk.gov.courtservice.xhibit.client.actions.results;

// Java
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.results.DisposalDialog;
import uk.gov.courtservice.xhibit.client.results.ResultsRowValue;
import uk.gov.courtservice.xhibit.client.results.disposals.AggravatingReasonsModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DisposalController;
import uk.gov.courtservice.xhibit.client.results.disposals.HateCrimeModel;
import uk.gov.courtservice.xhibit.client.results.disposals.OffencePanelModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalReferenceValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: The action event for the AddDisposalAction
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

public class AddDisposalAction extends XAction {

    private static final long serialVersionUID = 1L;

    private XhibitApplicationController xac;

    private ResultsRowValue rrv;

    private DisposalDialog disposalDialog;

    private DisposalController dc;
    
    private DisposalActionHelper actionHelper;
    
    private DeportationModel deportationReasons;
    
    private HateCrimeModel hateCrimeReasons;
    
    private AggravatingReasonsModel aggravatingReasons;

    public AddDisposalAction() {
        populateFromBundle("AddDisposal");
        this.setEnabled(false);

    }

    
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        actionHelper = new DisposalActionHelper();
        
        if (getModel() != null) {
        	DisposalReferenceValue drv = null;
            dc = ((OffencePanelModel) getModel()).getDisposalController();
            rrv = ((OffencePanelModel) getModel()).getResultRowValue();
            if (rrv != null) {                
                deportationReasons = actionHelper.getDeportationReasonFromDefendantOnCase(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                hateCrimeReasons = actionHelper.getHateCrimeReasons(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                aggravatingReasons = actionHelper.getAggravatingReasons(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                boolean hateCrimePanelAvailable = actionHelper.getHateIndicator(rrv.getCaseType(), rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                boolean aggravatingPanelAvailable = actionHelper.getAggravatingIndicator();
                if (rrv.getDefendantValue() != null) {
                    // If defendantOnCaseId contains value then Row is
                    // Unrelated(on Case) else it's related(on Offence)
                    if (rrv.getDefendantOnOffenceId() != null) {
                        disposalDialog = new DisposalDialog(xac, rrv.getDefendantOnOffenceId(), true, deportationReasons, hateCrimeReasons, hateCrimePanelAvailable, 
                        		aggravatingReasons, aggravatingPanelAvailable);
                    } else {
                        disposalDialog = new DisposalDialog(xac, rrv.getDefendantOnCaseId(), false, deportationReasons, hateCrimeReasons, hateCrimePanelAvailable,
                        		aggravatingReasons, aggravatingPanelAvailable);
                    }
  
                    disposalDialog.setVisible(true);
                    
                    // User has hit cancel so not continuing.
                    if (disposalDialog.isCancelClicked())
                        throw new UserCancelException();

                    //Assign updated/new values to the rrv
                    if (rrv.getDisposalValue() == null) {
                        rrv.setDisposalValue(disposalDialog.getValue());
                        rrv.setDisposalReferenceValue(disposalDialog.getReference());
                        rrv.setAction(ResultsRowValue.RESULT_ADD);
                        rrv.setModified(true);
                        drv = rrv.getDisposalReferenceValue();
                        dc.refreshData();
                    } else {
                        ResultsRowValue newRrv = (ResultsRowValue) rrv.clone();
                        newRrv.setDisposalValue(disposalDialog.getValue());
                        newRrv.setDisposalReferenceValue(disposalDialog.getReference());
                        newRrv.setAction(ResultsRowValue.RESULT_ADD);
                        newRrv.setModified(true);
                        java.util.List rowList = dc.getSelectedList();
                        int index = rowList.indexOf(rrv);
                        rowList.add(index + 1, newRrv);
                        drv = newRrv.getDisposalReferenceValue();
                        dc.refreshInsertedData(index + 1);
                        
                    }
 
                    
                    // ctx-1643 update the xhb_defendant_on_case table with the custodial,
    				// suspended, drug offence and deportation values.
    				// update defOnCaseBV with deportation values.
                    
    				DefendantOnCaseBasicValue defOnCaseBV = new DefendantOnCaseBasicValue();

    				defOnCaseBV.setDefendantOnCaseId(rrv.getDefendantOnCaseId());
     				defOnCaseBV.setCustodial(drv.getCustodial());
    				defOnCaseBV.setSuspended(drv.getSuspended());
    				defOnCaseBV.setSeriousDrugOffence(drv.getSeriousDrugOffence());
    				defOnCaseBV.setRecommendedDeportation(drv.getRecommendedDeportation());
    				
    				if (drv.isHateCrimeFlag()) {
    					defOnCaseBV.setHateSentIndicator("Y");
    				} else {
    					defOnCaseBV.setHateSentIndicator("N");
    				}
     				
		    		defOnCaseBV.setGeneralDisability(drv.isGeneralTransgender());
		    		defOnCaseBV.setGeneralSexual(drv.isGeneralSexual());
		    		defOnCaseBV.setGeneralTransgender(drv.isGeneralTransgender());
		    		defOnCaseBV.setRaceAndReligionAggravated(drv.isRaceAndReligionAggravated());
		    		defOnCaseBV.setRacialAggravated(drv.isRacialAggravated());
		    		defOnCaseBV.setReligionAggravated(drv.isReligionAggravated());
		    		defOnCaseBV.setVictimDisability(drv.isVictimDisability());
		    		defOnCaseBV.setVictimSexual(drv.isVictimSexual());
		    		defOnCaseBV.setVictimTransgender(drv.isVictimTransgender());
                    
		    		// Aggravating Reasons
		    		defOnCaseBV.setAggravatingAssaultOnWorkers(drv.isAggravatingAssaultOnWorkers());
		    		defOnCaseBV.setAggravatingTerroristConnection(drv.isAggravatingTerroristConnection());
		            defOnCaseBV.setAggravatingEmergencyWorkers(drv.isAggravatingEmergencyWorkers());
		            defOnCaseBV.setAggravatingHostility(drv.isAggravatingHostility());
		            defOnCaseBV.setAggravatingSexualOrientation(drv.isAggravatingSexualOrientation());
		            defOnCaseBV.setAggravatingSexualOrientationOfVictim(drv.isAggravatingSexualOrientationOfVictim());
		            defOnCaseBV.setAggravatingTransgender(drv.isAggravatingTransgender());
		            defOnCaseBV.setAggravatingTransgenderOfVictim(drv.isAggravatingTransgenderOfVictim());
                    
		    		ChargeControllerBeanBusinessDelegate chargesBD = XhibitDelegateHelper.getChargeDelegate();
		    		chargesBD.updateDefOnCaseDeportation(defOnCaseBV,
		    				XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
		    		
                    // dc.refreshData();
                }
            }
        }
    }

   
}