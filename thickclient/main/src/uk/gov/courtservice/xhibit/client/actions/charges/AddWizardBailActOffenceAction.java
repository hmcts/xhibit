package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.actions.search.AbstractSearchAction;
import uk.gov.courtservice.xhibit.client.actions.search.SearchProcessHandler;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddedOffencesPanelModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargeWizardModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AddWizardBailActOffenceAction
 * </p>
 * <p>
 * Description: Wizard for selecting only Bail Act Offences from XHIBIT search over Offences.
 * </p>
 *
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */


public class AddWizardBailActOffenceAction extends XAction implements SearchProcessHandler {

    private static final long serialVersionUID = 1L;

    private XPanel addedOffencesPanel;

    private XhibitApplicationController xac;
    private AddedOffencesPanelModel model;
    private ChargeWizardModel chargeWizardModel;
    private ChargesControllerModel ccm;
    
    public AddWizardBailActOffenceAction() {
        populateFromBundle("AddWizardBailActOffence");
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
                (XhibitApplicationController) getController(), XhibitActions.OpenSearchBailActOffence);
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
            
            DefendantOnOffenceComplexValue defOnOffComplexValue = offenceValue.getDefendantOnOffence(chargeWizardModel.getDefendantID());
            if(defOnOffComplexValue == null) {
            	defOnOffComplexValue = new DefendantOnOffenceComplexValue();
            }

            HashMap<Integer, DefendantOnOffenceComplexValue> defendantsOnOffencesComplexValues = new HashMap<Integer, DefendantOnOffenceComplexValue>();
            defendantsOnOffencesComplexValues.put(chargeWizardModel.getDefendantID(), defOnOffComplexValue);
            // Set defendant on offence
            offenceValue.setDefOnOffenceBasicValues(defendantsOnOffencesComplexValues);
            //Need to add seq no to list
            Integer nextSeqNo;
            List<Integer> seqNoList = ccm.getAllDefendantsOnCaseSeqNosSortedMap().get(chargeWizardModel.getDefendantOnCaseID());
            if (seqNoList.size() > 0) {
                nextSeqNo = new Integer(Integer.parseInt(seqNoList.get(seqNoList.size() - 1).toString()) + 1);
            } else {
                nextSeqNo = new Integer(1);
            }
            seqNoList.add(nextSeqNo);
      
            // The charge id will not exist for BAO in the process of being created.
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
