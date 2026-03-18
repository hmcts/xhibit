package uk.gov.courtservice.xhibit.client.actions.results;

// Java
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
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
import uk.gov.courtservice.xhibit.common.results.vos.SortedList;


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

public class EditDisposalAction extends XAction {
 
    private static final long serialVersionUID = 1L;

    private XhibitApplicationController xac;

    private ResultsRowValue rrv;

    private DisposalController dc;

    private DisposalDialog disposalDialog;
    
    private DisposalActionHelper actionHelper;

    private DeportationModel deportationReasons;
    
    private HateCrimeModel hateCrimeReasons;
    
    private AggravatingReasonsModel aggravatingReasons;
    
    public EditDisposalAction() {
        populateFromBundle("EditDisposal");
    }
    
    

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        xac = (XhibitApplicationController) getController();
        if (getModel() != null) {
            dc = ((OffencePanelModel) getModel()).getDisposalController();
            rrv = ((OffencePanelModel) getModel()).getResultRowValue();
                        
            if (rrv != null) {
                actionHelper = new DisposalActionHelper();
                
                if (rrv.getDisposalValue() != null && rrv.getDisposalReferenceValue() != null) // no
                // need
                // but
                // double-checking
                {
                    deportationReasons = actionHelper.getDeportationReasonFromDefendantOnCase(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                    hateCrimeReasons = actionHelper.getHateCrimeReasons(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                    aggravatingReasons = actionHelper.getAggravatingReasons(rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                    boolean hateCrimePanelAvailable = actionHelper.getHateIndicator(rrv.getCaseType(), rrv.getDefendantValue().getDefendantID(), rrv.getCaseId());
                    boolean aggravatingPanelAvailable = actionHelper.getAggravatingIndicator();
                    
                    disposalDialog = new DisposalDialog(xac, rrv.getDisposalReferenceValue(), rrv.getDisposalValue(), deportationReasons, hateCrimeReasons, hateCrimePanelAvailable,
                    		aggravatingReasons, aggravatingPanelAvailable);
                    disposalDialog.setVisible(true);
                    
//                  User has hit cancel so not continuing.
                    if (disposalDialog.isCancelClicked())
                        throw new UserCancelException();
                    
                    // Get list of disposal line values from before edit was performed
                    HashMap map = (HashMap) rrv.getDisposalValue().getMap().getMap();
                    Iterator it = map.entrySet().iterator();
                    ArrayList<XhbDisposalLineBasicValue> oldArrLine = new ArrayList<XhbDisposalLineBasicValue>();
                    
                    while (it.hasNext()) {
                    	Map.Entry pair = (Map.Entry)it.next();
                    	if(pair.getValue() instanceof SortedList) {
                    		SortedList list = (SortedList) pair.getValue();
                    		for(int i=0;i<list.size();i++){
                    			oldArrLine.add((XhbDisposalLineBasicValue)list.get(i));
                    		}
                    	} else {
                    		oldArrLine.add((XhbDisposalLineBasicValue) pair.getValue());                    	
                    	}
                    }

                    rrv.setDisposalValue(disposalDialog.getValue());
                    
                    // Get list of disposal line values from after edit was performed, now manually set the changed info
                    map = (HashMap) rrv.getDisposalValue().getMap().getMap();
                    it = map.entrySet().iterator();
                    ArrayList<XhbDisposalLineBasicValue> newArrLine = new ArrayList<XhbDisposalLineBasicValue>();
                    
                    while (it.hasNext()) {
                    	Map.Entry pair = (Map.Entry)it.next();
                    	if(pair.getValue() instanceof SortedList) {
                    		SortedList list = (SortedList) pair.getValue();
                    		for(int i=0;i<list.size();i++){
                       			newArrLine.add((XhbDisposalLineBasicValue) list.get(i));         
                    		}
                    	} else {
                   			newArrLine.add((XhbDisposalLineBasicValue) pair.getValue());         
                    	}
                    }
                    
                    // Manually add in the missing information to relevant disposal lines
                    for (int i = 0; i < oldArrLine.size(); i++) {
                    	for (int j = 0; j < newArrLine.size(); j++) {
                    		if (oldArrLine.get(i).getRefDisposalLineId().equals(newArrLine.get(j).getRefDisposalLineId()) 
                    				&& oldArrLine.get(i).getLineNumber().equals(newArrLine.get(j).getLineNumber())) {
                            	oldArrLine.get(i).setDelG1(newArrLine.get(j).getDelG1());
                            	oldArrLine.get(i).setDelG2(newArrLine.get(j).getDelG2());
                            	oldArrLine.get(i).setDelLineData(newArrLine.get(j).getDelLineData());
                            	oldArrLine.get(i).setLineData(newArrLine.get(j).getLineData());
                            	oldArrLine.get(i).setLineNumber(newArrLine.get(j).getLineNumber());
                            	oldArrLine.get(i).setObsInd(newArrLine.get(j).getObsInd());
                            	rrv.getDisposalValue().setLine(oldArrLine.get(i));
                            	break;
                    		} else{ 
                    			//will set it to obsolete , if its found above then obviously the obsolete will be set above
                    			if(!oldArrLine.get(i).getObsInd().equals("Y") && oldArrLine.get(i).getDisposalLineId()!=null){
                    				oldArrLine.get(i).setObsInd("Y");
                    			}
                    		}
                    	}
                    }
                    //only add in the obsolete rows that are valid obsolete rows (i.e. ones that need to be obsoleted from db)
                    for(int i=0;i<oldArrLine.size();i++) {
                    	if(oldArrLine.get(i).getObsInd().equals("Y") && oldArrLine.get(i).getDisposalLineId()!=null) {
                        	rrv.getDisposalValue().addLine(oldArrLine.get(i));
                    	}
                    }
                    
                    rrv.setDisposalReferenceValue(disposalDialog.getReference());
                    
                    if (rrv.getAction() != ResultsRowValue.RESULT_ADD) {
                        rrv.setAction(ResultsRowValue.RESULT_UPDATE);
                        rrv.setModified(true);
                    }
                   
                    // ctx-1643 update the xhb_defendant_on_case table with the custodial,
    				// suspended, drug offence and deportation values.
    				// update defOnCaseBV with deportation values.
                    
    				DefendantOnCaseBasicValue defOnCaseBV = new DefendantOnCaseBasicValue();

    				defOnCaseBV.setDefendantOnCaseId(rrv.getDefendantOnCaseId());
     				defOnCaseBV.setCustodial(rrv.getDisposalReferenceValue().getCustodial());
    				defOnCaseBV.setSuspended(rrv.getDisposalReferenceValue().getSuspended());
    				defOnCaseBV.setSeriousDrugOffence(rrv.getDisposalReferenceValue().getSeriousDrugOffence());
    				defOnCaseBV.setRecommendedDeportation(rrv.getDisposalReferenceValue().getRecommendedDeportation());
    				
     				if (rrv.getDisposalReferenceValue().isHateCrimeFlag()) {
    					defOnCaseBV.setHateSentIndicator("Y");
    				} else {
    					defOnCaseBV.setHateSentIndicator("N");
    				}
     				
		    		defOnCaseBV.setGeneralDisability(rrv.getDisposalReferenceValue().isGeneralTransgender());
		    		defOnCaseBV.setGeneralSexual(rrv.getDisposalReferenceValue().isGeneralSexual());
		    		defOnCaseBV.setGeneralTransgender(rrv.getDisposalReferenceValue().isGeneralTransgender());
		    		defOnCaseBV.setRaceAndReligionAggravated(rrv.getDisposalReferenceValue().isRaceAndReligionAggravated());
		    		defOnCaseBV.setRacialAggravated(rrv.getDisposalReferenceValue().isRacialAggravated());
		    		defOnCaseBV.setReligionAggravated(rrv.getDisposalReferenceValue().isReligionAggravated());
		    		defOnCaseBV.setVictimDisability(rrv.getDisposalReferenceValue().isVictimDisability());
		    		defOnCaseBV.setVictimSexual(rrv.getDisposalReferenceValue().isVictimSexual());
		    		defOnCaseBV.setVictimTransgender(rrv.getDisposalReferenceValue().isVictimTransgender());
                    
		    		// Aggravating Reasons
		    		defOnCaseBV.setAggravatingAssaultOnWorkers(rrv.getDisposalReferenceValue().isAggravatingAssaultOnWorkers());
		    		defOnCaseBV.setAggravatingTerroristConnection(rrv.getDisposalReferenceValue().isAggravatingTerroristConnection());
		            defOnCaseBV.setAggravatingEmergencyWorkers(rrv.getDisposalReferenceValue().isAggravatingEmergencyWorkers());
		            defOnCaseBV.setAggravatingHostility(rrv.getDisposalReferenceValue().isAggravatingHostility());
		            defOnCaseBV.setAggravatingSexualOrientation(rrv.getDisposalReferenceValue().isAggravatingSexualOrientation());
		            defOnCaseBV.setAggravatingSexualOrientationOfVictim(rrv.getDisposalReferenceValue().isAggravatingSexualOrientationOfVictim());
		            defOnCaseBV.setAggravatingTransgender(rrv.getDisposalReferenceValue().isAggravatingTransgender());
		            defOnCaseBV.setAggravatingTransgenderOfVictim(rrv.getDisposalReferenceValue().isAggravatingTransgenderOfVictim());
		            
		    		ChargeControllerBeanBusinessDelegate chargesBD = XhibitDelegateHelper.getChargeDelegate();
		    		chargesBD.updateDefOnCaseDeportation(defOnCaseBV,
		    				XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
                    dc.refreshData();


                }
            }
        }
    }
   
}