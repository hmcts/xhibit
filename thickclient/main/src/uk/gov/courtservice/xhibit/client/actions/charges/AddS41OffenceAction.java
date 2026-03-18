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
import uk.gov.courtservice.xhibit.client.maintaincharges.HOProcCodeHelper;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Action which when fired will add a section 41 (summary) offence
 * </p>
 * <p>
 * Description: This action is only used on the charges screen to allow the user
 * to add a section 41 (summary) offence.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 * 
 * Change Log
 * @version 1.1 - Frederik Vandendrie ssche - Rework to work with Iteration 2
 *          Search functionality.
 */
public class AddS41OffenceAction extends XAction implements SearchProcessHandler {

    /**
     * Creates an add section 41 (summary) offence action.
     */
    public AddS41OffenceAction() {
        populateFromBundle("AddS41Offence");
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

    /**
     * SearchProcessHandler implementation called when the user has selected an
     * offence from the search results.
     * 
     * @param searchAction
     *            the search action.
     * @throws CSRecoverableException
     */
    public void processResults(AbstractSearchAction searchAction) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChargesController chargesController = (ChargesController) xac.getBodyPanel();
        ChargesControllerModel ccm = chargesController.getModel();
        
        Collection col = searchAction.getResults();
        Iterator it = col.iterator();
        if (it.hasNext()) {
            ChargeControllerBeanBusinessDelegate chargesBD = ccm.getDelegate();
            RefOffenceBasicValue refOffence = (RefOffenceBasicValue) it.next();

            Integer caseID = ccm.getACM().getCaseId();
            Integer courtID = new Integer(ccm.getCourtId());

            OffenceValue offenceValue = new OffenceValue();
            // Set Minimum information
            offenceValue.setCaseID(caseID);
            offenceValue.setCourtID(courtID);
            // offenceValue.setPlea("G"); //Bug XI4037
            offenceValue.setRefOffenceID(refOffence.getId());
            offenceValue.setOffenceDescription(refOffence.getOffenceDesc());
            offenceValue.setStatute(refOffence.getStatute());
            offenceValue.setActSection(refOffence.getActSection());
            offenceValue.setCourtLogDate(Calendar.getInstance());

            //Add Offence Details and appropriate HO Proc Code
            AddOffenceDetailsDialog addOffenceDetailsDialog = new AddOffenceDetailsDialog(
                    AddOffenceDetailsDialog.TITLE.OFFENCE, offenceValue, xac, HOProcCodeHelper.S41,
                    ChargesControllerHelper.MODE.ADD);
            addOffenceDetailsDialog.setVisible(true);
            
            if(addOffenceDetailsDialog.isCancelClicked()) {
                throw new UserCancelException();
            }

            ccm.setOffenceValue(offenceValue);

            //Add Defenfant on Offence details
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

            if (ccm.isSection41ChargeCreated()) {
                offenceValue.setChargeID(ccm.getChargeValue().getChargeID());
                chargesBD.addOffence(offenceValue,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            } else {
                Vector offences = new Vector();
                offences.addElement(offenceValue);

                ChargeValue chargeValue = new ChargeValue(caseID, ChargeTypes.getChargeType("O"));
                chargeValue.setOffenceValues(offences);
                chargeValue.setCourtLogDate(Calendar.getInstance());
                chargeValue.setCourtID(courtID);
                chargesBD.addChargeToCase(chargeValue, xac.getApplicationCaseModel().getScheduledHearingId()==null?false:true,
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
            
            // Refresh Charges tab
            chargesController.loadCharges();
        } else {
            throw new UserCancelException();
        }
    }

    /**
     * Get a resource string from the Additional resources
     * 
     * @param key
     *            the key to lookup
     * @return the resource from the given key.
     */
    private String getString(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.AddCountsDefendantsResources, key);
    }
}