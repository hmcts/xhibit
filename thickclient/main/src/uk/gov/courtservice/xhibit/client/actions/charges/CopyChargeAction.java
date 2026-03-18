package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddBreachOffenceDefendantDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddDefendantsToOffenceDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddOffenceDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.BreachWizardModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.HOProcCodeHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;

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
 */

public class CopyChargeAction extends XAction {
    /**
	 * default serialversion uid
	 */
	private static final long serialVersionUID = 1L;

	public CopyChargeAction() {
        populateFromBundle("CopyCharge");
    }

	   public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
		  
           XhibitApplicationController xac = (XhibitApplicationController) getController();
           ChargesController chargesController = (ChargesController) xac.getBodyPanel();
           ChargesControllerModel model = chargesController.getModel();
            ChargeControllerBeanBusinessDelegate chargesBD = model.getDelegate();
            Integer chargeID = model.getChargeValue().getChargeID();
            Integer caseID = model.getACM().getCaseId();
            Integer courtID = new Integer(model.getCourtId());
            

            OffenceValue offenceValue = new OffenceValue();
            offenceValue.setCaseID(caseID);
            offenceValue.setCourtID(courtID);
            offenceValue.setRefOffenceID(model.getOffenceValue().getRefOffenceID());
            offenceValue.setOffenceDescription(model.getOffenceValue().getOffenceDescription());
            offenceValue.setCourtLogDate(Calendar.getInstance());

            //needed for indictment
            if(model.getChargeValue().getChargeType().equals("I")) {
            	offenceValue.setChargeID(chargeID);
            	offenceValue.setOffenceCode(model.getOffenceValue().getOffenceCode());
            	indictmentCopy(model, xac, offenceValue, chargesBD);
            	//Summary offences
            } else if(model.getChargeValue().getChargeType().equals("O")){
	            //needed for summary offence 
	            offenceValue.setStatute(model.getOffenceValue().getStatute());
	            offenceValue.setActSection(model.getOffenceValue().getActSection());
	            summaryOffenceCopy(model, xac, offenceValue, chargesBD);
	            //committal for sentence
            } else if(model.getChargeValue().getChargeType().equals("S")) {
            	 //needed for summary offence 
	            offenceValue.setStatute(model.getOffenceValue().getStatute());
	            offenceValue.setActSection(model.getOffenceValue().getActSection());
            	committalForSentenceCopy(model, xac, offenceValue, chargesBD);
            } else if(model.getChargeValue().getChargeType().equals("C")) {
            	offenceValue.setStatute(model.getOffenceValue().getStatute());
 	            offenceValue.setActSection(model.getOffenceValue().getActSection());
 	            appealOffencesCopy(model, xac, offenceValue, chargesBD);
            } else if(model.getChargeValue().getChargeType().equals("B")) {
            	offenceValue.setChargeID(chargeID);
                offenceValue.setOffenceCode(model.getOffenceValue().getOffenceCode());
            	breachesOffenceCopy(model, xac, offenceValue, chargesBD);
            }
            //Refresh Charges tab
            chargesController.loadCharges();
	    }

	   private void breachesOffenceCopy(ChargesControllerModel model, XhibitApplicationController xac,
			OffenceValue offenceValue, ChargeControllerBeanBusinessDelegate chargesBD) throws CSRecoverableException {
		   
		    BreachWizardModel chargeWizardModel = new BreachWizardModel();
		    chargeWizardModel.setBreachValue(model.getBreachValue());
		    chargeWizardModel.setAddedOffences(new ArrayList<OffenceValue>());
		    chargeWizardModel.setCaseID(model.getACM().getCaseId());
		    chargeWizardModel.setCaseType(model.getACM().getCaseType());
		    chargeWizardModel.setChargeType(ChargeTypes.BREACH);
		    chargeWizardModel.setChargeID(model.getChargeValue().getChargeID());
		    chargeWizardModel.setCourtId(XhibitSingleton.getInstance().getCourtId());
		    chargeWizardModel.setDefendantID(model.getChargeValue().getDefendantID());
		    chargeWizardModel.setDefendantOnCaseID(model.getDefendantValue().getDefOnCaseBasicValue().getId());
		    
		 //Launch Add Additional Breach Offence Defendants details functionality for Breach Wizard
		   AddBreachOffenceDefendantDetailsDialog addBreachOffenceDefendantDetailsDialog = 
               new AddBreachOffenceDefendantDetailsDialog(xac, offenceValue, chargeWizardModel, false, 
                       ChargesControllerHelper.MODE.ADD, model);
           
           addBreachOffenceDefendantDetailsDialog.setVisible(true);
           if (addBreachOffenceDefendantDetailsDialog.isCancelClicked()) 
               throw new UserCancelException();
          Vector<Integer> defendants = new Vector<Integer>();
          if (model.getChargeValue().getDefendantID() == null) {
        	  throw new CSRecoverableException("gui.user.addbreachoffence.nodefendant", "gui.log.addbreachoffence.nodefendant");
          } else {
        	  defendants.add(model.getChargeValue().getDefendantID());
          }
          offenceValue.setDefendantIDs(defendants);

          model.getDelegate().addOffence(offenceValue,
                   		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           
	}

	/**
	    * Create the charge value for the appeal offences
	    * @param model ChargesControllerModel
	    * @param xac XhibitApplicationController
	    * @param offenceValue OffenceValue
	    * @param chargesBD ChargeControllerBeanBusinessDelegate
	    * @throws CSRecoverableException
	    */
	   private void appealOffencesCopy(ChargesControllerModel model, XhibitApplicationController xac,
			OffenceValue offenceValue, ChargeControllerBeanBusinessDelegate chargesBD) throws CSRecoverableException {
		   //Add Offence Details and HO Proc Code is null for appeal offence
           AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                   AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, null,
                   ChargesControllerHelper.MODE.ADD);
           addOffenceDetailsDialog.setVisible(true);
           
           if (addOffenceDetailsDialog.isCancelClicked())
               throw new UserCancelException();
           
           model.setOffenceValue(offenceValue);

           AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, false,
                   AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, model.getCCV().getAllDefendants(),
                   ChargesControllerHelper.MODE.ADD);

           addDefendantsToOffenceDialog.setVisible(true);
           
           if (addDefendantsToOffenceDialog.isCancelClicked())
               throw new UserCancelException();
           
           // Need to retrieve selected defendants from Dialog
           offenceValue.setDefendantIDs(addDefendantsToOffenceDialog.getDefendantIDs());

           // Set DefendantOnOffenceComplexValues on the OffenceValue.
           if (addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues() == null) {
               throw new UserCancelException();
           }
           // Set defendants to offence value
           offenceValue.setDefOnOffenceBasicValues(addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues());
           
           if (model.isAppealChargeCreated()) {
               offenceValue.setChargeID(model.getChargeValue().getChargeID());
               chargesBD.addOffence(offenceValue,
               		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           } else {
               Vector<OffenceValue> offences = new Vector<OffenceValue>();
               offences.add(offenceValue);

               ChargeValue chargeValue = new ChargeValue(offenceValue.getCaseID(), ChargeTypes.getChargeType("C"));
               chargeValue.setOffenceValues(offences);
               chargeValue.setCourtLogDate(Calendar.getInstance());
               
               chargesBD.addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
               		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           }
		
	}

	/**
	    * Create the charge value for committal for sentence
	    * @param model ChargesControllerModel
	    * @param xac XhibitApplicationController
	    * @param offenceValue OffenceValue
	    * @param chargesBD ChargeControllerBeanBusinessDelegate
	    * @throws CSRecoverableException
	    */
	   private void committalForSentenceCopy(ChargesControllerModel model, XhibitApplicationController xac,
			OffenceValue offenceValue, ChargeControllerBeanBusinessDelegate chargesBD) throws CSRecoverableException {
		   AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                   AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, HOProcCodeHelper.SENT,
                   ChargesControllerHelper.MODE.ADD);
           addOffenceDetailsDialog.setVisible(true);
           
           if (addOffenceDetailsDialog.isCancelClicked())
               throw new UserCancelException();

           model.setOffenceValue(offenceValue);

           // NO DEFENDANTS FOUND
           AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, false,
                   AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, model.getCCV().getAllDefendants(),
                   ChargesControllerHelper.MODE.ADD);

           addDefendantsToOffenceDialog.setVisible(true);
           
           if (addDefendantsToOffenceDialog.isCancelClicked())
               throw new UserCancelException();

           // Need to retrieve selected defendants from Dialog
           offenceValue.setDefendantIDs(addDefendantsToOffenceDialog.getDefendantIDs());

           // Set DefendantOnOffenceComplexValues on the OffenceValue.
           if (addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues() == null) {
               throw new UserCancelException();
           }
           // Set defendants to offence value
           offenceValue.setDefOnOffenceBasicValues(addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues());

           if (model.isCommittalChargeCreated()) {
               offenceValue.setChargeID(model.getChargeValue().getChargeID());
               chargesBD.addOffence(offenceValue,
               		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           } else {
               Vector<OffenceValue> offences = new Vector<OffenceValue>();
               offences.add(offenceValue);

               ChargeValue chargeValue = new ChargeValue(offenceValue.getCaseID(), ChargeTypes.getChargeType("S"));
               chargeValue.setOffenceValues(offences);
               chargeValue.setCourtLogDate(Calendar.getInstance());
               //ctx-1834 updated court id to get the refSystemCode
               chargeValue.setCourtID(offenceValue.getCourtID());
               chargesBD.addChargeToCase(chargeValue,xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
               		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           }
        
	}



	/**
	    * Create the charge value for Summary offences
	    * @param model ChargesControllerModel
	    * @param xac XhibitApplicationController
	    * @param offenceValue OffenceValue
	    * @param chargesBD ChargeControllerBeanBusinessDelegate
	    * @throws CSRecoverableException
	    */
	   private void summaryOffenceCopy(ChargesControllerModel model, XhibitApplicationController xac,
			OffenceValue offenceValue, ChargeControllerBeanBusinessDelegate chargesBD) throws CSRecoverableException {
		   //If Summary Offences
           AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac,
          HOProcCodeHelper.S41, ChargesControllerHelper.MODE.ADD);
           
           addOffenceDetailsDialog.setVisible(true);
           if( addOffenceDetailsDialog.isCancelClicked())
                   throw new UserCancelException();

           //If summary offences
           model.setOffenceValue(offenceValue);

           //Add Defendant on Offence details
           AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, false,
                   AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, model.getCCV().getAllDefendants(),
                   ChargesControllerHelper.MODE.ADD);
           addDefendantsToOffenceDialog.setVisible(true);

           if (addDefendantsToOffenceDialog.isCancelClicked())
               throw new UserCancelException();

           // Need to retrieve selected defendants from Dialog
           offenceValue.setDefendantIDs(addDefendantsToOffenceDialog.getDefendantIDs());

           // Set DefendantOnOffenceComplexValues on the OffenceValue.
           if (addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues() == null) {
               throw new UserCancelException();
           }
           
           // Set defendants to offence value
           offenceValue.setDefOnOffenceBasicValues(addDefendantsToOffenceDialog.getDefendantOnOffenceComplexValues());

           if (model.isSection41ChargeCreated()) {
               offenceValue.setChargeID(model.getChargeValue().getChargeID());
               chargesBD.addOffence(offenceValue,
               		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           } else {
               Vector<OffenceValue> offences = new Vector<OffenceValue>();
               offences.addElement(offenceValue);

               ChargeValue chargeValue = new ChargeValue(offenceValue.getCaseID(), ChargeTypes.getChargeType("O"));
               chargeValue.setOffenceValues(offences);
               chargeValue.setCourtLogDate(Calendar.getInstance());
               chargeValue.setCourtID(offenceValue.getCourtID());
               chargesBD.addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
               		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
           }
          

	}

	/**
	    * Create the charge value for an indictment copy
	    * @param model ChargesControllerModel
	    * @param xac XhibitApplicationController
	    * @param offenceValue OffenceValue
	    * @param chargesBD ChargeControllerBeanBusinessDelegate
	    * @param chargesController ChargesController
	    * @throws CSRecoverableException
	    */
	private void indictmentCopy(ChargesControllerModel model, XhibitApplicationController xac,
			OffenceValue offenceValue, ChargeControllerBeanBusinessDelegate chargesBD) throws CSRecoverableException {
		 //If Indictment
           AddOffenceDetailsDialog addOffenceDetailsDialog = 
               new AddOffenceDetailsDialog(AddOffenceDetailsDialog.TITLE.COUNT, offenceValue, xac, HOProcCodeHelper.TRIAL, ChargesControllerHelper.MODE.ADD);
           addOffenceDetailsDialog.setVisible(true);

           if (addOffenceDetailsDialog.isCancelClicked())
               throw new UserCancelException();
        //if Indictment
        chargesBD.addOffence(offenceValue,
        		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));

        // Log that a count has been added.
        CrestIndictmentLog.getInstance().addCountLog(
                xac.getApplicationCaseModel().getScheduledHearingValue().getCaseBasicValue(), offenceValue);
		
	}
}