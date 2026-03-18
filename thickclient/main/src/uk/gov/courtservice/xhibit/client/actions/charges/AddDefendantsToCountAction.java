package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.client.maintaincharges.AddDefendantsToOffenceDialog;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerHelper;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesControllerModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Add Defendant to a selected count.
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

public class AddDefendantsToCountAction extends XAction {

    public AddDefendantsToCountAction() {
        populateFromBundle("AddDefendantsToCount");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        ChargesController chargesController;
        ChargesControllerModel ccm;

        xac = (XhibitApplicationController) getController();
        chargesController = (ChargesController) xac.getBodyPanel();
        ccm = chargesController.getModel();

        /*
         * CCN0257 This field is now optional //Check if additional Offence End
         * date present before continuing if
         * (ccm.getOffenceValue().getOffenceEndDateTime() == null) {
         * JOptionPane.showMessageDialog(xac,
         * getString("addDefendantOnOffence.noEndDate.msg"),
         * getString("addDefendantToCount.title"),
         * JOptionPane.INFORMATION_MESSAGE); throw new UserCancelException(); }
         */

        Collection notOnOffence = null;
        if(ccm.getChargeValue().getChargeType().equals("I")){
        	notOnOffence = ChargesControllerHelper.getDefendantsNotOnOffenceICases(ccm);
        }
        else {
        	notOnOffence = ChargesControllerHelper.getDefendantsNotOnOffence(ccm);
        }
        Collection onOffence = ChargesControllerHelper.getDefendantsOnOffence(ccm);
        if (notOnOffence.size() > 0) {
            //Always regenerate SeqNo hashmap to ignore original charges
            regenerateSeqNoList(ccm,xac);
            try
            {
                AddDefendantsToOffenceDialog addDefendantsToOffenceDialog = new AddDefendantsToOffenceDialog(xac, true,
                        AddDefendantsToOffenceDialog.TITLE.DEFENDANT_TO_COUNT, notOnOffence,
                        ChargesControllerHelper.MODE.ADD);
                addDefendantsToOffenceDialog.setVisible(true);
            }finally{
                //always reload charges so that seqNoList is re-constructed taking into account original charges if applicable.
                chargesController.loadCharges();
            }


        } else {
            JOptionPane.showMessageDialog(xac, getString("addDefendantToCount.noDefendants.msg"),
                    getString("addDefendantToCount.title"), JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void regenerateSeqNoList(ChargesControllerModel ccm,XhibitApplicationController xac)throws CSRecoverableException{
        //Regenerate the seqnolists to ignore original charges, for all defendants.
        ChargeCompositeValue ccv = ccm.getCCV();
        Collection charges = null;
        
        try{
            charges = XhibitDelegateHelper.getChargeDelegate().getChargesList(xac.getApplicationCaseModel().getCaseId());
        }catch (Exception e){
            throw new CSRecoverableException("gui.OriginalChargesAction.reloadDefOnCaseSeqNosMap",
                    "Exception whilst getting the charge composite value object from the mid tier", e);
        }
        
        HashMap<Integer,List> defOnCaseSeqNosMap = SeqNoHelper.createSequenceNosMap(ccv.getAllDefendants());
        
        Iterator iterator = charges.iterator();
        while (iterator.hasNext()) {
            ChargeValue chargeValue = (ChargeValue) iterator.next();
            if (!chargeValue.getChargeType().equals(ChargeTypes.ORIGINAL_CHARGE.getChargeType())){
                //Add it to the list as long as its not an 'Original Charge'
                SeqNoHelper.processSequenceNos(chargeValue,defOnCaseSeqNosMap);
            }
            
        }
        //Assign the new value to the model
        ccm.setAllDefOnCaseSeqNosMap(defOnCaseSeqNosMap);
    
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