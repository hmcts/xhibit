package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddDefendantsToOffenceDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddOffenceDetailsDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class AddAppealOffenceAction extends XAction implements SearchProcessHandler{

    /**
     * Creates an add committal for sentence offence action.
     */
    public AddAppealOffenceAction() {
        populateFromBundle("AddAppealOffence");
    }

    /**
     * XAction implementation called when the action is fired.
     * 
     * @param e
     *            the action event.
     * @throws CSRecoverableException
     * @throws Exception
     */
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        AbstractSearchAction sa = (AbstractSearchAction) XhibitActions.getAction(
                (XhibitApplicationController) getController(), XhibitActions.OpenSearchOffence);
        sa.setCaller(this);
        sa.xActionPerformed(e);
    }

	@Override
	public void processResults(AbstractSearchAction asa) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        
        Collection col = asa.getResults();
        Iterator it = col.iterator();
        if (it.hasNext()) {
	        ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();
	        RefOffenceBasicValue refOffence = (RefOffenceBasicValue) it.next();
	        Integer caseID = ccm.getACM().getCaseId();
	        Integer courtID = new Integer(ccm.getCourtId());
	
	        OffenceValue offenceValue = new OffenceValue();
	        offenceValue.setCaseID(caseID);
	        offenceValue.setCourtID(courtID);
	        offenceValue.setRefOffenceID(refOffence.getId());
	        offenceValue.setOffenceDescription(refOffence.getOffenceDesc());
	        offenceValue.setStatute(refOffence.getStatute());
	        offenceValue.setActSection(refOffence.getActSection());
	        offenceValue.setCourtLogDate(Calendar.getInstance());	
        	
            //Add Offence Details and HO Proc Code is null for appeal offence
            AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                    AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, null,
                    ChargesControllerHelper.MODE.ADD);
            addOffenceDetailsDialog.setVisible(true);
            
            if (addOffenceDetailsDialog.isCancelClicked())
                throw new UserCancelException();
            
            ccm.setOffenceValue(offenceValue);

            AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, false,
                    AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_OFFENCE, ccm.getCCV().getAllDefendants(),
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
            
            if (ccm.isAppealChargeCreated()) {
                offenceValue.setChargeID(ccm.getChargeValue().getChargeID());
                chargesBD.addOffence(offenceValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            } else {
                Vector offences = new Vector();
                offences.add(offenceValue);

                ChargeValue chargeValue = new ChargeValue(caseID, ChargeTypes.getChargeType("C"));
                chargeValue.setOffenceValues(offences);
                chargeValue.setCourtLogDate(Calendar.getInstance());
                
                chargesBD.addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
            // Refresh Charges tab
            chargesController.loadCharges();        	
        } else {
            new UserCancelException();
        }         
	}
}

